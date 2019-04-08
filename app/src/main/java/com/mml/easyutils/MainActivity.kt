package com.mml.easyutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mml.android.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        write.setOnClickListener {
            LogUtils.debug(true).saveSd(true).i(msg = "sssss")
        }
    }
}
