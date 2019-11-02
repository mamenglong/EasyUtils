package com.mml.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/4/8.
 * 修改时间：2019/4/8 11:03
 */

@SuppressLint("StaticFieldLeak")
object EasyUtilsApplication : Application() {

    /**
     * Global application context.
     */
    private var mContext: Context? = null
    /**
     * 是否debug模式
     */
    var isDebug:Boolean=false

    override fun onCreate() {
        super.onCreate()
        mContext = this

    }

    /**
     * Get the global application context.
     *
     * @return Application context.
     * @throws
     */
    fun getContext(): Context {
        if (mContext == null) {
            throw Exception("please check if you had initialized EasyUtilsApplication/EasyUtils in your Application .")
        }
        return mContext!!
    }
    fun setContext(context: Context)= kotlin.run { mContext=context }
}