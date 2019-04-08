package com.mml.android.utils

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresPermission
import com.mml.android.EasyUtilsApplication


/**
 * 项目名称：ADBWIFICONNECT
 * Created by Long on 2019/3/14.
 * 修改时间：2019/3/14 15:38
 */
class WIFIUtils private constructor(private val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        var instance: WIFIUtils? = null

        fun getInstance(mContext: Context): WIFIUtils? {
            if (instance == null) {
                synchronized(WIFIUtils::class) {
                    if (instance == null) {
                        instance =
                            WIFIUtils(EasyUtilsApplication.getContext())
                    }
                }
            }
            return instance
        }
    }

    /**
     * 判断当前网络是否是4G网络
     *
     * @param
     * @return boolean
     */
    @RequiresPermission(allOf = [ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE])
    fun is4GAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        if (activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE) {
            val telephonyManager = context.getSystemService(
                Context.TELEPHONY_SERVICE
            ) as TelephonyManager
            val networkType = telephonyManager.networkType
            /** Current network is LTE  */
            if (networkType == 13) {
                /**此时的网络是4G的 */
                return true
            }
        }
        return false
    }

    /**
     * 判断wifi是否可用
     */

    fun isWifiAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected && networkInfo
            .type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 检查wifi是否处开连接状态
     *
     * @return
     */
    fun isWifiConnect(): Boolean {
        val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val mWifiInfo = connManager!!.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return mWifiInfo.isConnected
    }

    fun getWifiIpAddress(): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        //判断wifi是否开启
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        val ip = intToIp(ipAddress)
        Log.i("WIFIManager.Class", "$ipAddress")
        return ip
    }

    private fun intToIp(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)
    }

}