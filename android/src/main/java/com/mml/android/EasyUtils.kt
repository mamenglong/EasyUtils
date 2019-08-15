package com.mml.android

import android.annotation.SuppressLint
import android.content.Context

/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/4/8.
 * 修改时间：2019/4/8 11:18
 */
@SuppressLint("StaticFieldLeak")
object EasyUtils {

    /**
     * Global application context.
     */

    @JvmStatic
    fun initialize(context: Context) {
        EasyUtilsApplication.setContext(context)
    }
    @JvmStatic
    fun debug(isDebug:Boolean) {
        EasyUtilsApplication.isDebug = isDebug
    }
}