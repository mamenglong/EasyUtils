package com.mml.easyutils

import android.os.Bundle
import android.os.Handler
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.mml.android.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TIME = 100//这里默认每隔100毫秒添加一个气泡

    internal var mHandler = Handler()
    internal var runnable: Runnable = object : Runnable {

        override fun run() {
            // handler自带方法实现定时器
            try {
                mHandler.postDelayed(this, TIME.toLong())
                balloonRelativeLayout.addBalloon()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        write.setOnClickListener {
            LogUtils.debug(true).saveSd(true).i(msg = "sssss")
        }
        showballoon.setOnClickListener {
            balloonRelativeLayout.visibility =
                balloonRelativeLayout.visibility.let {
                    if (it == VISIBLE) {
                        showballoon.text = "显示气泡"
                        INVISIBLE
                    } else {
                        showballoon.text = "隐藏气泡"
                        balloonRelativeLayout.removeAllViews()
                        VISIBLE
                    }
                }

            mHandler.postDelayed(runnable, TIME.toLong())
        }
    }
}
