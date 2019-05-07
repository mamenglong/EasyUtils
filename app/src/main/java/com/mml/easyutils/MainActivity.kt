package com.mml.easyutils

import android.os.Bundle
import android.os.Handler
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.mml.android.utils.LogUtils
import com.mml.easyutils.model.User
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val sp by lazy {
        SP(this)
    }
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
                        GONE
                    } else {
                        showballoon.text = "隐藏气泡"
                        balloonRelativeLayout.removeAllViews()
                        VISIBLE
                    }
                }

            mHandler.postDelayed(runnable, TIME.toLong())

        }

        val arr = arrayOf("aa", "aab", "aac")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
        autotv.setAdapter(arrayAdapter)


        sp_test.setOnClickListener {
            sp.user = User("nihao")
            val ss = sp.user as User
            log.text.append(ss.name + "\n")
        }
    }
}
