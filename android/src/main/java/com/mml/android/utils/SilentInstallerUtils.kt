package com.mml.android.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import java.io.File

/**
 * 项目名称：Library
 * Created by Long on 2018/10/15.
 * 修改时间：2018/10/15 13:14
 * @author Long
 */
class SilentInstallerUtils {


    /**
     * 获取当前系统安装应用的默认位置
     *
     * @return APP_INSTALL_AUTO or APP_INSTALL_INTERNAL or APP_INSTALL_EXTERNAL.
     */
    val installLocation: Int
        get() {
            val commandResult = ShellUtils.execCmd(
                "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm get-install-location", false, true
            )
            if (commandResult.result == 0 && commandResult.successMsg != null && commandResult.successMsg.length > 0) {
                try {
                    return Integer.parseInt(commandResult.successMsg.substring(0, 1))
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }

            }
            return APP_INSTALL_AUTO
        }

    /**
     * get params for pm install location
     *
     * @return
     */
    private val installLocationParams: String
        get() {
            val location = installLocation
            when (location) {
                APP_INSTALL_INTERNAL -> return "-f"
                APP_INSTALL_EXTERNAL -> return "-s"
            }
            return ""
        }

    /**
     * uninstall package silent by root
     *
     * **Attentions:**
     *  * Don't call this on the ui thread, it may costs some times.
     *  * You should add **android.permission.DELETE_PACKAGES** in manifest, so no need to request root
     * permission, if you are system app.
     *
     *
     * @param context     file path of package
     * @param packageName package name of app
     * @param isKeepData  whether keep the data and cache directories around after package removal
     * @return
     *  * [.DELETE_SUCCEEDED] means uninstall success
     *  * [.DELETE_FAILED_INTERNAL_ERROR] means internal error
     *  * [.DELETE_FAILED_INVALID_PACKAGE] means package name error
     *  * [.DELETE_FAILED_PERMISSION_DENIED] means permission denied
     */
    @JvmOverloads
    fun uninstallSilent(context: Context, packageName: String?, isKeepData: Boolean = false): Int {
        if (packageName == null || packageName.length == 0) {
            return DELETE_FAILED_INVALID_PACKAGE
        }

        /**
         * if context is system app, don't need root permission, but should add <uses-permission android:name="android.permission.DELETE_PACKAGES"></uses-permission> in mainfest
         */
        val command = StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall")
            .append(if (isKeepData) " -k " else " ").append(packageName.replace(" ", "\\ "))
        val commandResult = ShellUtils.execCmd(command.toString(), !isSystemApplication(context), true)
        if (commandResult.successMsg != null && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains(
                "success"
            ))
        ) {
            return DELETE_SUCCEEDED
        }
        Log.e(
            TAG,
            StringBuilder().append("uninstallSilent successMsg:").append(commandResult.successMsg)
                .append(", ErrorMsg:").append(commandResult.errorMsg).toString()
        )
        if (commandResult.errorMsg == null) {
            return DELETE_FAILED_INTERNAL_ERROR
        }
        return if (commandResult.errorMsg.contains("Permission denied")) {
            DELETE_FAILED_PERMISSION_DENIED
        } else DELETE_FAILED_INTERNAL_ERROR
    }

    /**
     * * install package silent by root
     *
     * **Attentions:**
     *  * Don't call this on the ui thread, it may costs some times.
     *  * You should add **android.permission.INSTALL_PACKAGES** in manifest, so no need to request root
     * permission, if you are system app.
     *
     */
    @JvmOverloads
    fun installSilent(context: Context, filePath: String?, pmParams: String? = " -r $installLocationParams"): Int {
        if (filePath == null || filePath.length == 0) {
            return INSTALL_FAILED_INVALID_URI
        }

        val file = File(filePath)
        if (file == null || file.length() <= 0 || !file.exists() || !file.isFile) {
            return INSTALL_FAILED_INVALID_URI
        }

        /**
         * if context is system app, don't need root permission, but should add <uses-permission android:name="android.permission.INSTALL_PACKAGES"></uses-permission> in mainfest
         */
        val command = StringBuilder().append("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install ")
            .append(pmParams ?: "").append(" ").append(filePath.replace(" ", "\\ "))
        val commandResult = ShellUtils.execCmd(
            command.toString(),
            !isSystemApplication(context), true
        )
        if (commandResult.successMsg != null && (commandResult.successMsg.contains("Success") || commandResult.successMsg.contains(
                "success"
            ))
        ) {
            return INSTALL_SUCCEEDED
        }

        Log.e(
            TAG,
            StringBuilder().append("installSilent successMsg:").append(commandResult.successMsg)
                .append(", ErrorMsg:").append(commandResult.errorMsg).toString()
        )
        if (commandResult.errorMsg == null) {
            return INSTALL_FAILED_OTHER
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_ALREADY_EXISTS")) {
            return INSTALL_FAILED_ALREADY_EXISTS
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_APK")) {
            return INSTALL_FAILED_INVALID_APK
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_URI")) {
            return INSTALL_FAILED_INVALID_URI
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INSUFFICIENT_STORAGE")) {
            return INSTALL_FAILED_INSUFFICIENT_STORAGE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DUPLICATE_PACKAGE")) {
            return INSTALL_FAILED_DUPLICATE_PACKAGE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NO_SHARED_USER")) {
            return INSTALL_FAILED_NO_SHARED_USER
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UPDATE_INCOMPATIBLE")) {
            return INSTALL_FAILED_UPDATE_INCOMPATIBLE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_SHARED_USER_INCOMPATIBLE")) {
            return INSTALL_FAILED_SHARED_USER_INCOMPATIBLE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_SHARED_LIBRARY")) {
            return INSTALL_FAILED_MISSING_SHARED_LIBRARY
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_REPLACE_COULDNT_DELETE")) {
            return INSTALL_FAILED_REPLACE_COULDNT_DELETE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_DEXOPT")) {
            return INSTALL_FAILED_DEXOPT
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_OLDER_SDK")) {
            return INSTALL_FAILED_OLDER_SDK
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONFLICTING_PROVIDER")) {
            return INSTALL_FAILED_CONFLICTING_PROVIDER
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_NEWER_SDK")) {
            return INSTALL_FAILED_NEWER_SDK
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_TEST_ONLY")) {
            return INSTALL_FAILED_TEST_ONLY
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CPU_ABI_INCOMPATIBLE")) {
            return INSTALL_FAILED_CPU_ABI_INCOMPATIBLE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MISSING_FEATURE")) {
            return INSTALL_FAILED_MISSING_FEATURE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_CONTAINER_ERROR")) {
            return INSTALL_FAILED_CONTAINER_ERROR
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_INVALID_INSTALL_LOCATION")) {
            return INSTALL_FAILED_INVALID_INSTALL_LOCATION
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_MEDIA_UNAVAILABLE")) {
            return INSTALL_FAILED_MEDIA_UNAVAILABLE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_TIMEOUT")) {
            return INSTALL_FAILED_VERIFICATION_TIMEOUT
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_VERIFICATION_FAILURE")) {
            return INSTALL_FAILED_VERIFICATION_FAILURE
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_PACKAGE_CHANGED")) {
            return INSTALL_FAILED_PACKAGE_CHANGED
        }
        if (commandResult.errorMsg.contains("INSTALL_FAILED_UID_CHANGED")) {
            return INSTALL_FAILED_UID_CHANGED
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NOT_APK")) {
            return INSTALL_PARSE_FAILED_NOT_APK
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_MANIFEST")) {
            return INSTALL_PARSE_FAILED_BAD_MANIFEST
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION")) {
            return INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_NO_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_NO_CERTIFICATES
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES")) {
            return INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING")) {
            return INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME")) {
            return INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID")) {
            return INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_MALFORMED")) {
            return INSTALL_PARSE_FAILED_MANIFEST_MALFORMED
        }
        if (commandResult.errorMsg.contains("INSTALL_PARSE_FAILED_MANIFEST_EMPTY")) {
            return INSTALL_PARSE_FAILED_MANIFEST_EMPTY
        }
        return if (commandResult.errorMsg.contains("INSTALL_FAILED_INTERNAL_ERROR")) {
            INSTALL_FAILED_INTERNAL_ERROR
        } else INSTALL_FAILED_OTHER
    }

    /**
     *
     * whether packageName is system application
     */
    fun isSystemApplication(context: Context): Boolean {
        val packageManager = context.getPackageManager()
        val packageName = context.getPackageName()
        if (packageManager == null || packageName == null || packageName!!.length == 0) {
            return false
        }
        try {
            val app = packageManager!!.getApplicationInfo(packageName, 0)
            return (app != null && (app!!.flags and ApplicationInfo.FLAG_SYSTEM) > 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    companion object {
        /**
         * Installation return code<br></br>
         * install success.
         */
        val INSTALL_SUCCEEDED = 1
        /**
         * Installation return code<br></br>
         * the package is already installed.
         */
        val INSTALL_FAILED_ALREADY_EXISTS = -1

        /**
         * Installation return code<br></br>
         * the package archive file is invalid.
         */
        val INSTALL_FAILED_INVALID_APK = -2

        /**
         * Installation return code<br></br>
         * the URI passed in is invalid.
         */
        val INSTALL_FAILED_INVALID_URI = -3

        /**
         * Installation return code<br></br>
         * the package manager service found that the device didn't have enough storage space to install the app.
         */
        val INSTALL_FAILED_INSUFFICIENT_STORAGE = -4

        /**
         * Installation return code<br></br>
         * a package is already installed with the same name.
         */
        val INSTALL_FAILED_DUPLICATE_PACKAGE = -5

        /**
         * Installation return code<br></br>
         * the requested shared user does not exist.
         */
        val INSTALL_FAILED_NO_SHARED_USER = -6

        /**
         * Installation return code<br></br>
         * a previously installed package of the same name has a different signature than the new package (and the old
         * package's data was not removed).
         */
        val INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7

        /**
         * Installation return code<br></br>
         * the new package is requested a shared user which is already installed on the device and does not have matching
         * signature.
         */
        val INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8

        /**
         * Installation return code<br></br>
         * the new package uses a shared library that is not available.
         */
        val INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9

        /**
         * Installation return code<br></br>
         * the new package uses a shared library that is not available.
         */
        val INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10

        /**
         * Installation return code<br></br>
         * the new package failed while optimizing and validating its dex files, either because there was not enough storage
         * or the validation failed.
         */
        val INSTALL_FAILED_DEXOPT = -11

        /**
         * Installation return code<br></br>
         * the new package failed because the current SDK version is older than that required by the package.
         */
        val INSTALL_FAILED_OLDER_SDK = -12

        /**
         * Installation return code<br></br>
         * the new package failed because it contains a content provider with the same authority as a provider already
         * installed in the system.
         */
        val INSTALL_FAILED_CONFLICTING_PROVIDER = -13

        /**
         * Installation return code<br></br>
         * the new package failed because the current SDK version is newer than that required by the package.
         */
        val INSTALL_FAILED_NEWER_SDK = -14

        /**
         * Installation return code<br></br>
         * the new package failed because it has specified that it is a test-only package and the caller has not supplied
         * the  flag.
         */
        val INSTALL_FAILED_TEST_ONLY = -15

        /**
         * Installation return code<br></br>
         * the package being installed contains native code, but none that is compatible with the the device's CPU_ABI.
         */
        val INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16

        /**
         * Installation return code<br></br>
         * the new package uses a feature that is not available.
         */
        val INSTALL_FAILED_MISSING_FEATURE = -17

        /**
         * Installation return code<br></br>
         * a secure container mount point couldn't be accessed on external media.
         */
        val INSTALL_FAILED_CONTAINER_ERROR = -18

        /**
         * Installation return code<br></br>
         * the new package couldn't be installed in the specified install location.
         */
        val INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19

        /**
         * Installation return code<br></br>
         * the new package couldn't be installed in the specified install location because the media is not available.
         */
        val INSTALL_FAILED_MEDIA_UNAVAILABLE = -20

        /**
         * Installation return code<br></br>
         * the new package couldn't be installed because the verification timed out.
         */
        val INSTALL_FAILED_VERIFICATION_TIMEOUT = -21

        /**
         * Installation return code<br></br>
         * the new package couldn't be installed because the verification did not succeed.
         */
        val INSTALL_FAILED_VERIFICATION_FAILURE = -22

        /**
         * Installation return code<br></br>
         * the package changed from what the calling program expected.
         */
        val INSTALL_FAILED_PACKAGE_CHANGED = -23

        /**
         * Installation return code<br></br>
         * the new package is assigned a different UID than it previously held.
         */
        val INSTALL_FAILED_UID_CHANGED = -24

        /**
         * Installation return code<br></br>
         * if the parser was given a path that is not a file, or does not end with the expected '.apk' extension.
         */
        val INSTALL_PARSE_FAILED_NOT_APK = -100

        /**
         * Installation return code<br></br>
         * if the parser was unable to retrieve the AndroidManifest.xml file.
         */
        val INSTALL_PARSE_FAILED_BAD_MANIFEST = -101

        /**
         * Installation return code<br></br>
         * if the parser encountered an unexpected exception.
         */
        val INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102

        /**
         * Installation return code<br></br>
         * if the parser did not find any certificates in the .apk.
         */
        val INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103

        /**
         * Installation return code<br></br>
         * if the parser found inconsistent certificates on the files in the .apk.
         */
        val INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104

        /**
         * Installation return code<br></br>
         * if the parser encountered a CertificateEncodingException in one of the files in the .apk.
         */
        val INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105

        /**
         * Installation return code<br></br>
         * if the parser encountered a bad or missing package name in the manifest.
         */
        val INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106

        /**
         * Installation return code<br></br>
         * if the parser encountered a bad shared user id name in the manifest.
         */
        val INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107

        /**
         * Installation return code<br></br>
         * if the parser encountered some structural problem in the manifest.
         */
        val INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108

        /**
         * Installation return code<br></br>
         * if the parser did not find any actionable tags (instrumentation or application) in the manifest.
         */
        val INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109

        /**
         * Installation return code<br></br>
         * if the system failed to install the package because of system issues.
         */
        val INSTALL_FAILED_INTERNAL_ERROR = -110
        /**
         * Installation return code<br></br>
         * other reason
         */
        val INSTALL_FAILED_OTHER = -1000000

        /**
         * Uninstall return code<br></br>
         * uninstall success.
         */
        val DELETE_SUCCEEDED = 1

        /**
         * Uninstall return code<br></br>
         * uninstall fail if the system failed to delete the package for an unspecified reason.
         */
        val DELETE_FAILED_INTERNAL_ERROR = -1

        /**
         * Uninstall return code<br></br>
         * uninstall fail if the system failed to delete the package because it is the active DevicePolicy manager.
         */
        val DELETE_FAILED_DEVICE_POLICY_MANAGER = -2

        /**
         * Uninstall return code<br></br>
         * uninstall fail if pcakge name is invalid
         */
        val DELETE_FAILED_INVALID_PACKAGE = -3

        /**
         * Uninstall return code<br></br>
         * uninstall fail if permission denied
         */
        val DELETE_FAILED_PERMISSION_DENIED = -4

        private val TAG = SilentInstallerUtils::class.java!!.getSimpleName()

        /**
         * App installation location flags of android system
         */
        val APP_INSTALL_AUTO = 0
        val APP_INSTALL_INTERNAL = 1
        val APP_INSTALL_EXTERNAL = 2
    }

}
/**
 * uninstall package and clear data of app silent by root
 *
 * @param context
 * @param packageName package name of app
 * @return
 * @see .uninstallSilent
 */
/**
 * install package silent by root
 *
 * **Attentions:**
 *  * Don't call this on the ui thread, it may costs some times.
 *  * You should add **android.permission.INSTALL_PACKAGES** in manifest, so no need to request root
 * permission, if you are system app.
 *  * Default pm install params is "-r".
 *
 *
 * @param context
 * @param filePath file path of package
 * @see .installSilent
 */
