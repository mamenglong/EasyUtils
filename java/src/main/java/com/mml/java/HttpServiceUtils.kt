package com.mml.java

/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/3/24.
 * 修改时间：2019/3/24 12:41
 */

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.sql.DriverManager.println
import java.util.concurrent.TimeUnit

object HttpServiceUtils {

    private var BASE_URL: String = ""
    private val httpClient = OkHttpClient.Builder()

    private var DEFAULT_TIMEOUT: Long = 20L
    private val builder = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())

    /***
     * 设置连接超时时间默认20s,此函数需在create（）之前调用
     * @param time
     */
    fun setTimeOut(time: Long): HttpServiceUtils {
        DEFAULT_TIMEOUT = time
        httpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
        return this
    }

    /**
     * 设置base url
     * @param url
     */
    fun setBaseURL(url: String): HttpServiceUtils {
        BASE_URL = url
        builder.baseUrl(BASE_URL)
        return this
    }

    fun buildClient(): Retrofit.Builder = builder.client(httpClient.build())

    /**
     * 是否设置链接日志过滤,此函数需在create（）之前调用
     * @param isUse
     */
    fun setIsUseLoggingInterceptor(isUse: Boolean): HttpServiceUtils {
        httpClient.addInterceptor(
            HttpLoggingInterceptor(
                HttpLoggingInterceptor
                    .Logger { message ->
                        //打印retrofit日志
                        println("RetrofitLog" + "retrofitBack = $message")
                    }
            )
                .setLevel(
                    if (isUse)
                        HttpLoggingInterceptor.Level.BODY
                    else
                        HttpLoggingInterceptor.Level.NONE
                )
        )
        return this
    }

    /***
     * 创建实例
     * @param serviceClass 接口类
     * @exception setBaseURL(url) must be called first.
     */
    fun <T> create(serviceClass: Class<T>): T = if (BASE_URL == "")
        throw RuntimeException("setBaseURL(url) must be called first.")
    else {
        buildClient().build().create(serviceClass)
    }
}