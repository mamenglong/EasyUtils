package com.mml.android.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.mml.android.EasyUtilsApplication
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/3/18.
 * 修改时间：2019/3/18 14:04
 * SharedPreferences 数据保存
 */
//private val preferences by lazy { SharedPreferencesUtils(this) }
//preferences.age = 56

open class SharedPreferencesUtils(context: Context) {

    companion object {
        lateinit var instances: SharedPreferencesUtils

        fun getInstance(context: Context): SharedPreferencesUtils? {
            if (instances == null) {
                synchronized(SharedPreferencesUtils::class) {
                    instances =
                        SharedPreferencesUtils(context)
                }
            }
            return instances
        }

        fun getInstance(): SharedPreferencesUtils {
            if (instances == null) {
                synchronized(SharedPreferencesUtils::class) {
                    instances =
                        SharedPreferencesUtils(EasyUtilsApplication.getContext())
                }
            }
            return instances
        }
    }

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

//    var username by SharedPreferenceDelegates.string(defaultValue = "this is username")
//
//    var age by SharedPreferenceDelegates.int()
//
//    var flag by SharedPreferenceDelegates.boolean()
//
//    var currentDateTime: Long by SharedPreferenceDelegates.long(System.currentTimeMillis())
//
//    var money by SharedPreferenceDelegates.float()
//
//    var setString by SharedPreferenceDelegates.setString()

    protected object SharedPreferenceDelegates {

        fun int(defaultValue: Int = 0) = object : ReadWriteProperty<SharedPreferencesUtils, Int> {

            override fun getValue(thisRef: SharedPreferencesUtils, property: KProperty<*>): Int {
                return thisRef.preferences.getInt(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesUtils, property: KProperty<*>, value: Int) {
                thisRef.preferences.edit().putInt(property.name, value).apply()
            }
        }

        fun long(defaultValue: Long = 0L) = object : ReadWriteProperty<SharedPreferencesUtils, Long> {

            override fun getValue(thisRef: SharedPreferencesUtils, property: KProperty<*>): Long {
                return thisRef.preferences.getLong(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesUtils, property: KProperty<*>, value: Long) {
                thisRef.preferences.edit().putLong(property.name, value).apply()
            }
        }

        fun boolean(defaultValue: Boolean = false) = object : ReadWriteProperty<SharedPreferencesUtils, Boolean> {
            override fun getValue(thisRef: SharedPreferencesUtils, property: KProperty<*>): Boolean {
                return thisRef.preferences.getBoolean(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesUtils, property: KProperty<*>, value: Boolean) {
                thisRef.preferences.edit().putBoolean(property.name, value).apply()
            }
        }

        fun float(defaultValue: Float = 0.0f) = object : ReadWriteProperty<SharedPreferencesUtils, Float> {
            override fun getValue(thisRef: SharedPreferencesUtils, property: KProperty<*>): Float {
                return thisRef.preferences.getFloat(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesUtils, property: KProperty<*>, value: Float) {
                thisRef.preferences.edit().putFloat(property.name, value).apply()
            }
        }

        fun string(defaultValue: String? = null) = object : ReadWriteProperty<SharedPreferencesUtils, String?> {
            override fun getValue(thisRef: SharedPreferencesUtils, property: KProperty<*>): String? {
                return thisRef.preferences.getString(property.name, defaultValue)
            }

            override fun setValue(thisRef: SharedPreferencesUtils, property: KProperty<*>, value: String?) {
                thisRef.preferences.edit().putString(property.name, value).apply()
            }
        }

        fun setString(defaultValue: Set<String>? = null) =
            object : ReadWriteProperty<SharedPreferencesUtils, Set<String>?> {
                override fun getValue(thisRef: SharedPreferencesUtils, property: KProperty<*>): Set<String>? {
                    return thisRef.preferences.getStringSet(property.name, defaultValue)
                }

                override fun setValue(thisRef: SharedPreferencesUtils, property: KProperty<*>, value: Set<String>?) {
                    thisRef.preferences.edit().putStringSet(property.name, value).apply()
                }
            }
    }
}
