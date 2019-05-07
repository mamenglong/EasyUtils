package com.mml.easyutils

import android.content.Context
import com.mml.android.utils.SharedPreferencesUtils

/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/5/7.
 * 修改时间：2019/5/7 23:35
 */
class SP(context: Context) : SharedPreferencesUtils(context) {

    var user by SharedPreferenceDelegates.Any()
}