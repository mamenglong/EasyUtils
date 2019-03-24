package com.mml.adbwifidebug.utils

/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/3/18.
 * 修改时间：2019/3/18 15:45
 */
import android.util.Log
import com.mml.lib.FileUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object LogUtil {


    private val TOP_BORDER =
        "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private val LEFT_BORDER = "║ "
    private val BOTTOM_BORDER =
        "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private var debug: Boolean = true//是否打印log
    private var savesd: Boolean = false//是否存log到sd卡
    private val CHUNK_SIZE = 106 //设置字节数
    private var logDir = ""//设置文件存储目录
    private var logSize = 2 * 1024 * 1024L//设置log文件大小 k
    private val execu: ExecutorService = Executors.newFixedThreadPool(1)

    init {
        initLogFile()
    }


    fun v(tag: String = "LogUtil", msg: String) = debug.log(tag, msg, Log.VERBOSE)
    fun d(tag: String = "LogUtil", msg: String) = debug.log(tag, msg, Log.DEBUG)
    fun i(tag: String = "LogUtil", msg: String) = debug.log(tag, msg, Log.INFO)
    fun w(tag: String = "LogUtil", msg: String) = debug.log(tag, msg, Log.WARN)
    fun e(tag: String = "LogUtil", msg: String) = debug.log(tag, msg, Log.ERROR)


    private fun targetStackTraceMSg(): String {
        val targetStackTraceElement = getTargetStackTraceElement()
        return if (targetStackTraceElement != null) {
            "at ${targetStackTraceElement.className}.${targetStackTraceElement.methodName}(${targetStackTraceElement.fileName}:${targetStackTraceElement.lineNumber})"
        } else {
            ""
        }
    }

    private fun getTargetStackTraceElement(): StackTraceElement? {
        var targetStackTrace: StackTraceElement? = null
        var shouldTrace = false
        val stackTrace = Thread.currentThread().stackTrace
        for (stackTraceElement in stackTrace) {
            val isLogMethod = stackTraceElement.className == LogUtil::class.java.name
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement
                break
            }
            shouldTrace = isLogMethod
        }
        return targetStackTrace
    }


    private fun initLogFile() {
        logDir = "${FileUtil.getRootDir()}/Logs"
    }

    private fun Boolean.log(tag: String, msg: String, type: Int) {
        if (!this) {//无视约定  错误直接输出
            if (type == Log.ERROR) {

                true.saveToSd(
                    "$type",
                    tag,
                    msg,
                    targetStackTraceMSg()
                )
                Log.e(tag, msgFormat(msg, targetStackTraceMSg()))
            }
            return
        }
        val targetStackTraceMSg = targetStackTraceMSg()
        val newMsg = msgFormat(msg, targetStackTraceMSg)

        savesd.saveToSd(
            "$type",
            tag,
            msg,
            targetStackTraceMSg
        )
        when (type) {
            Log.VERBOSE -> Log.v(tag, newMsg)
            Log.DEBUG -> Log.d(tag, newMsg)
            Log.INFO -> Log.i(tag, newMsg)
            Log.WARN -> Log.w(tag, newMsg)
            Log.ERROR -> Log.e(tag, newMsg)
        }

    }

    private fun msgFormat(msg: String, targetStackTraceMSg: String): String {
        val bytes: ByteArray = msg.toByteArray()
        val length = bytes.size
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        var newMsg = "$TOP_BORDER\n$LEFT_BORDER\t${sdf.format(Date())}\n$LEFT_BORDER\t$targetStackTraceMSg"
        if (length > CHUNK_SIZE) {
            var i = 0
            while (i < length) {
                val count = Math.min(length - i, CHUNK_SIZE)
                val tempStr = String(bytes, i, count)
                newMsg += "\n$LEFT_BORDER\t$tempStr"
                i += CHUNK_SIZE
            }
        } else {
            newMsg += "\n$LEFT_BORDER\tContent -->$msg"
        }
        newMsg += "\n$BOTTOM_BORDER"
        return newMsg

    }

    private fun Boolean.saveToSd(level: String, TAG: String, msg: String, targetStackTraceMSg: String) {
        if (!this) {
            return
        }
        FileUtil.mkDirs(logDir)
        execu.submit {
            val tag = "time=${SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.US
            ).format(Date())}\nfilter=$TAG\nlocation=$targetStackTraceMSg"
            val data = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()) + "_$level"
            val files = FileUtil.sortByTime(File(logDir))?.filter { it.name.contains(data) }
            val filepath: String
            if (files != null && files.isNotEmpty()) {
                val length: Long = FileUtil.getLeng(files[0])
                if (length > logSize) {
                    val id = files[0].name.replace("${data}_", "").replace(".log", "").toInt() + 1
                    filepath = "$logDir/${data}_$id.log"
                    FileUtil.creatFile(filepath)
                } else {
                    filepath = files[0].absolutePath
                }
            } else {
                filepath = "$logDir/${data}_1.log"
                FileUtil.creatFile(filepath)
            }
            val split = "------------------------------"
            FileUtil.appendText(File(filepath), "\r\n$tag\nmsg=$msg\n$split")
        }

    }


    /**
     * 是否打印log输出
     * @param debug
     */
    fun debug(debug: Boolean): LogUtil {
        LogUtil.debug = debug
        return this
    }

    /**
     * 是否保存到sd卡
     * @param savesd
     */
    fun saveSd(savesd: Boolean): LogUtil {
        LogUtil.savesd = savesd
        return this
    }

    /**
     * 设置每个log的文件大小
     * @param logSize 文件大小 byte
     */
    fun logSize(logSize: Long): LogUtil {
        LogUtil.logSize = logSize
        return this

    }

    /**
     * 设置log文件目录
     * @param logDir 文件目录
     */
    fun logDir(logDir: String): LogUtil {
        LogUtil.logDir = logDir
        return this
    }


}