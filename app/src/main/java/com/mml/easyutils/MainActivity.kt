package com.mml.easyutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mml.adbwifidebug.utils.LogUtil
import com.mml.easyutils.R
import com.mml.lib.A
import com.mml.lib.FileUtil
import com.mml.lib.network.ServiceCreator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        write.setOnClickListener {
            LogUtil.debug(true).saveSd(true).i(msg = "sssss")
        }
    }
}
