package com.mml.android.utils

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore


import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 项目名称：Library
 * Created by Long on 2018/10/15.
 * 修改时间：2018/10/15 13:14
 * @author Long
 */
class BitmapUtil {
    fun compressBitmapToBytes(filePath: String, reqWidth: Int, reqHeight: Int, quality: Int): ByteArray {
        val bitmap = getSmallBitmap(filePath, reqWidth, reqHeight)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        val bytes = baos.toByteArray()
        bitmap.recycle()
        LogUtils.i(TAG, "Bitmap compressed success, size: " + bytes.size)
        return bytes
    }

    fun compressBitmapSmallTo(filePath: String, reqWidth: Int, reqHeight: Int, maxLenth: Int): ByteArray {
        var quality = 100
        var bytes = compressBitmapToBytes(filePath, reqWidth, reqHeight, quality)
        while (bytes.size > maxLenth && quality > 0) {
            quality = quality / 2
            bytes = compressBitmapToBytes(filePath, reqWidth, reqHeight, quality)
        }
        return bytes
    }

    fun compressBitmapQuikly(filePath: String): ByteArray {
        return compressBitmapToBytes(filePath, 480, 800, 50)
    }

    fun compressBitmapQuiklySmallTo(filePath: String, maxLenth: Int): ByteArray {
        return compressBitmapSmallTo(filePath, 480, 800, maxLenth)
    }

    companion object {

        private val TAG = BitmapUtil::class.java.simpleName

        /**
         * convert Bitmap to byte array
         */
        fun bitmapToByte(b: Bitmap): ByteArray {
            val o = ByteArrayOutputStream()
            b.compress(Bitmap.CompressFormat.PNG, 100, o)
            return o.toByteArray()
        }

        /**
         * convert byte array to Bitmap
         */
        fun byteToBitmap(b: ByteArray?): Bitmap? {
            return if (b == null || b.size == 0) null else BitmapFactory.decodeByteArray(b, 0, b.size)
        }

        /**
         * 把bitmap转换成Base64编码String
         */
        fun bitmapToString(bitmap: Bitmap): String {
            return Base64.encodeToString(bitmapToByte(bitmap), Base64.DEFAULT)
        }

        /**
         * convert Drawable to Bitmap
         */
        fun drawableToBitmap(drawable: Drawable?): Bitmap? {
            return if (drawable == null) null else (drawable as BitmapDrawable).bitmap
        }

        /**
         * convert Bitmap to Drawable
         */
        fun bitmapToDrawable(bitmap: Bitmap?): Drawable? {
            return if (bitmap == null) null else BitmapDrawable(bitmap)
        }

        /**
         * scale image
         */
        fun scaleImageTo(org: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
            return scaleImage(org, newWidth.toFloat() / org.width, newHeight.toFloat() / org.height)
        }

        /**
         * scale image
         */
        fun scaleImage(org: Bitmap?, scaleWidth: Float, scaleHeight: Float): Bitmap? {
            if (org == null) {
                return null
            }
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            return Bitmap.createBitmap(org, 0, 0, org.width, org.height, matrix, true)
        }

        fun toRoundCorner(bitmap: Bitmap): Bitmap {
            val height = bitmap.height
            val width = bitmap.height
            val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(output)

            val paint = Paint()
            val rect = Rect(0, 0, width, height)

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            //paint.setColor(0xff424242);
            paint.color = Color.TRANSPARENT
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }

        fun createBitmapThumbnail(bitMap: Bitmap, needRecycle: Boolean, newHeight: Int, newWidth: Int): Bitmap {
            val width = bitMap.width
            val height = bitMap.height
            // 计算缩放比例
            val scaleWidth = newWidth.toFloat() / width
            val scaleHeight = newHeight.toFloat() / height
            // 取得想要缩放的matrix参数
            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)
            // 得到新的图片
            val newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true)
            if (needRecycle) {
                bitMap.recycle()
            }
            return newBitMap
        }

        fun saveBitmap(bitmap: Bitmap?, file: File): Boolean {
            if (bitmap == null) {
                return false
            }
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return false
        }

        fun saveBitmap(bitmap: Bitmap, absPath: String): Boolean {
            return saveBitmap(bitmap, File(absPath))
        }

        fun buildImageGetIntent(saveTo: Uri, outputX: Int, outputY: Int, returnData: Boolean): Intent {
            return buildImageGetIntent(saveTo, 1, 1, outputX, outputY, returnData)
        }

        fun buildImageGetIntent(
            saveTo: Uri, aspectX: Int, aspectY: Int,
            outputX: Int, outputY: Int, returnData: Boolean
        ): Intent {
            LogUtils.i(TAG, "Build.VERSION.SDK_INT : " + Build.VERSION.SDK_INT)
            val intent = Intent()
            if (Build.VERSION.SDK_INT < 19) {
                intent.action = Intent.ACTION_GET_CONTENT
            } else {
                intent.action = Intent.ACTION_OPEN_DOCUMENT
                intent.addCategory(Intent.CATEGORY_OPENABLE)
            }
            intent.type = "image/*"
            intent.putExtra("output", saveTo)
            intent.putExtra("aspectX", aspectX)
            intent.putExtra("aspectY", aspectY)
            intent.putExtra("outputX", outputX)
            intent.putExtra("outputY", outputY)
            intent.putExtra("scale", true)
            intent.putExtra("return-data", returnData)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
            return intent
        }

        fun buildImageCropIntent(uriFrom: Uri, uriTo: Uri, outputX: Int, outputY: Int, returnData: Boolean): Intent {
            return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData)
        }

        fun buildImageCropIntent(
            uriFrom: Uri, uriTo: Uri, aspectX: Int, aspectY: Int,
            outputX: Int, outputY: Int, returnData: Boolean
        ): Intent {
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(uriFrom, "image/*")
            intent.putExtra("crop", "true")
            intent.putExtra("output", uriTo)
            intent.putExtra("aspectX", aspectX)
            intent.putExtra("aspectY", aspectY)
            intent.putExtra("outputX", outputX)
            intent.putExtra("outputY", outputY)
            intent.putExtra("scale", true)
            intent.putExtra("return-data", returnData)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
            return intent
        }

        fun buildImageCaptureIntent(uri: Uri): Intent {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            return intent
        }

        fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val h = options.outHeight
            val w = options.outWidth
            var inSampleSize = 0
            if (h > reqHeight || w > reqWidth) {
                val ratioW = w.toFloat() / reqWidth
                val ratioH = h.toFloat() / reqHeight
                inSampleSize = Math.min(ratioH, ratioW).toInt()
            }
            inSampleSize = Math.max(1, inSampleSize)
            return inSampleSize
        }

        fun getSmallBitmap(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options)
        }
    }
}
