package com.mml.android.utils

import android.hardware.Camera
import android.os.Build
import android.os.Handler

/**
 * 项目名称：Library
 * Created by Long on 2018/10/15.
 * 修改时间：2018/10/15 13:14
 * @author Long
 */

/**
 *
 * Call requires API level 5
 * <uses-permission android:name="android.permission.FLASHLIGHT"></uses-permission>
 * <uses-permission android:name="android.permission.CAMERA"></uses-permission>
 */
class FlashLightUtils {

    private var camera: Camera? = null
    private val handler = Handler()

    fun turnOnFlashLight(): Boolean {
        if (camera == null) {
            camera = Camera.open()
            camera!!.startPreview()
            val parameter = camera!!.parameters
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                parameter.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            } else {
                parameter.set("flash-mode", "torch")
            }
            camera!!.parameters = parameter
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({ turnOffFlashLight() }, OFF_TIME.toLong())
        }
        return true
    }

    fun turnOffFlashLight(): Boolean {
        if (camera != null) {
            handler.removeCallbacksAndMessages(null)
            val parameter = camera!!.parameters
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                parameter.flashMode = Camera.Parameters.FLASH_MODE_OFF
            } else {
                parameter.set("flash-mode", "off")
            }
            camera!!.parameters = parameter
            camera!!.stopPreview()
            camera!!.release()
            camera = null
        }
        return true
    }

    companion object {

        /**
         * 超过3分钟自动关闭，防止损伤硬件
         */
        private val OFF_TIME = 3 * 60 * 1000
    }
}
