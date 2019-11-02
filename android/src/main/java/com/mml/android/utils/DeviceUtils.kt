package com.mml.android.utils

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.mml.android.EasyUtilsApplication
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/3/18.
 * 修改时间：2019/3/18 14:07
 * desc  : utils about device
 */

object DeviceUtils {

    /**
     * Return whether device is rooted.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = android.os.Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val `in` = BufferedReader(InputStreamReader(process!!.inputStream))
            return `in`.readLine() != null
        } catch (t: Throwable) {
            return false
        } finally {
            process?.destroy()
        }
    }


    /**
     * Return whether ADB is enabled.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun isAdbEnabled(): Boolean {
        return Settings.Secure.getInt(
            EasyUtilsApplication.getContext().contentResolver,
            Settings.Global.ADB_ENABLED, 0
        ) > 0
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”,可空
     */
    fun getSystemLanguage(): String? {
        return Locale.getDefault().language
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    fun getSystemLanguageList(): Array<Locale> {
        return Locale.getAvailableLocales()
    }

    /**
     * Return the version name of device's system.
     * 系统版本号
     * @return the version name of device's system
     */
    fun getSDKVersionName(): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * Return version code of device's system.
     *
     * @return version code of device's system
     */
    fun getSDKVersionCode() = android.os.Build.VERSION.SDK_INT

    /**
     * Return the android id of device.
     *
     * @return the android id of device
     */
    @SuppressLint("HardwareIds")
    fun getAndroidID(): String {
        return Settings.Secure.getString(
            EasyUtilsApplication.getContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    /**
     * Return the MAC address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return the MAC address
     */
    @RequiresPermission(allOf = [ACCESS_WIFI_STATE, INTERNET])
    fun getMacAddress(): String {
        return getMacAddress(*null as Array<String>)
    }

    /**
     * Return the MAC address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return the MAC address
     */
    @RequiresPermission(allOf = [ACCESS_WIFI_STATE, INTERNET])
    fun getMacAddress(vararg excepts: String): String {
        var macAddress = getMacAddressByNetworkInterface()
        if (isAddressNotInExcepts(macAddress, *excepts)) {
            return macAddress
        }
        macAddress = getMacAddressByInetAddress()
        if (isAddressNotInExcepts(macAddress, *excepts)) {
            return macAddress
        }
        macAddress = getMacAddressByWifiInfo()
        if (isAddressNotInExcepts(macAddress, *excepts)) {
            return macAddress
        }
        macAddress = getMacAddressByFile()
        return if (isAddressNotInExcepts(macAddress, *excepts)) {
            macAddress
        } else ""
    }


    private fun isAddressNotInExcepts(address: String, vararg excepts: String): Boolean {
        if (excepts.isEmpty()) {
            return "02:00:00:00:00:00" != address
        }
        for (filter in excepts) {
            if (address == filter) {
                return false
            }
        }
        return true
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getMacAddressByWifiInfo(): String {
        try {
            val wifi = EasyUtilsApplication.getContext()
                .applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val info = wifi.connectionInfo
            if (info != null) return info.macAddress
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getMacAddressByNetworkInterface(): String {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                if (ni == null || !ni.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = ni.hardwareAddress
                if (macBytes != null && macBytes.isNotEmpty()) {
                    val sb = StringBuilder()
                    for (b in macBytes) {
                        sb.append(String.format("%02x:", b))
                    }
                    return sb.substring(0, sb.length - 1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getMacAddressByInetAddress(): String {
        try {
            val inetAddress = getInetAddress()
            if (inetAddress != null) {
                val ni = NetworkInterface.getByInetAddress(inetAddress)
                if (ni != null) {
                    val macBytes = ni.hardwareAddress
                    if (macBytes != null && macBytes.size > 0) {
                        val sb = StringBuilder()
                        for (b in macBytes) {
                            sb.append(String.format("%02x:", b))
                        }
                        return sb.substring(0, sb.length - 1)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return "02:00:00:00:00:00"
    }

    private fun getInetAddress(): InetAddress? {
        try {
            val nis = NetworkInterface.getNetworkInterfaces()
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement()
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp) continue
                val addresses = ni.inetAddresses
                while (addresses.hasMoreElements()) {
                    val inetAddress = addresses.nextElement()
                    if (!inetAddress.isLoopbackAddress) {
                        val hostAddress = inetAddress.hostAddress
                        if (hostAddress.indexOf(':') < 0) return inetAddress
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getMacAddressByFile(): String {
        var result = ShellUtils.execCmd("getprop wifi.interface", false)
        if (result.result === 0) {
            val name = result.successMsg
            if (name != null) {
                result = ShellUtils.execCmd("cat /sys/class/net/$name/address", false)
                if (result.result === 0) {
                    val address = result.successMsg
                    if (address != null && address!!.length > 0) {
                        return address
                    }
                }
            }
        }
        return "02:00:00:00:00:00"
    }

    /**
     * Return the manufacturer of the product/hardware.
     * <p>e.g. Xiaomi</p>
     *厂商
     * @return the manufacturer of the product/hardware
     */
    fun getManufacturer(): String {
        return Build.MANUFACTURER
    }

    /**
     * 获取手机品牌
     *
     * @return  手机品牌
     */
    fun getDeviceBrand(): String {
        return Build.BRAND
    }

    /**
     * Return the model of device.
     * <p>e.g. MI2SC</p>
     * 手机型号
     * @return the model of device
     */
    fun getModel(): String {
        var model: String? = Build.MODEL
        model = model?.trim { it <= ' ' }?.replace("\\s*".toRegex(), "") ?: ""
        return model
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    @SuppressLint("MissingPermission")
    fun getIMEI(): String? {
        val tm: TelephonyManager =
            EasyUtilsApplication.getContext().getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceId
    }

    /**
     * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
     * element in the list.
     *
     * @return an ordered list of ABIs supported by this device
     */
    @RequiresPermission(allOf = [READ_PHONE_STATE])
    fun getABIs(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
            } else arrayOf(Build.CPU_ABI)
        }
    }

    /**
     * Shutdown the device
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.SHUTDOWN/>}
     * in manifest.</p>
     */
    fun shutdown() {
        ShellUtils.execCmd("reboot -p", true)
        val intent = Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN")
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false)
        EasyUtilsApplication.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * Reboot the device.
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"} in manifest.</p>
     */
    fun reboot() {
        ShellUtils.execCmd("reboot", true)
        val intent = Intent(Intent.ACTION_REBOOT)
        intent.putExtra("nowait", 1)
        intent.putExtra("interval", 1)
        intent.putExtra("window", 0)
        EasyUtilsApplication.getContext().sendBroadcast(intent)
    }

    /**
     * Reboot the device.
     * <p>Requires root permission
     * or hold {@code android:sharedUserId="android.uid.system"},
     * {@code <uses-permission android:name="android.permission.REBOOT" />}</p>
     *
     * @param reason code to pass to the kernel (e.g., "recovery") to
     *               request special boot modes, or null.
     */
    fun reboot(reason: String) {
        val pm = EasyUtilsApplication.getContext().getSystemService(Context.POWER_SERVICE) as PowerManager

        pm.reboot(reason)
    }

    /**
     * Reboot the device to recovery.
     * <p>Requires root permission.</p>
     */
    fun reboot2Recovery() {
        ShellUtils.execCmd("reboot recovery", true)
    }

    /**
     * Reboot the device to bootloader.
     * <p>Requires root permission.</p>
     */
    fun reboot2Bootloader() {
        ShellUtils.execCmd("reboot bootloader", true)
    }
}