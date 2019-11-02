package com.mml.android.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.mml.android.R


/**
 * 项目名称：EasyUtils
 * Created by Long on 2019/4/13.
 * 修改时间：2019/4/13 22:47
 */
class MYAutoCompleteTextView : AutoCompleteTextView, View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private var mClearIcon: Drawable? = null
    private var mExpendMoreIcon: Drawable? = null
    private var mExpendLessIcon: Drawable? = null
    private var mOnTouchListener: View.OnTouchListener? = null
    private var mOnFocusChangeListener: View.OnFocusChangeListener? = null
    //是否获得焦点，在输入
    private var SHOWCLOSE = false
    // 是否显示了展开
    private var SHOWEXPEND = true

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x.toInt()
        if (mClearIcon!!.isVisible && x > width - paddingRight - mClearIcon!!.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                SHOWEXPEND = true
                SHOWCLOSE = false
                setText("")
                setIconVisible()
            }
            return true
        }
        if (mExpendMoreIcon!!.isVisible && x > width - paddingRight - mClearIcon!!.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                SHOWCLOSE = false
                SHOWEXPEND = false
                showDropDown()
                setIconVisible()
            }
            return true
        }
        if (mExpendLessIcon!!.isVisible && x > width - paddingRight - mClearIcon!!.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                SHOWEXPEND = true
                SHOWCLOSE = false
                setIconVisible()
                dismissDropDown()
            }
            return true
        }
        return mOnTouchListener != null && mOnTouchListener!!.onTouch(view, motionEvent)
    }
    /**
     * [OnFocusChangeListener]
     */

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            SHOWCLOSE = (!text.isEmpty()).apply {
                SHOWEXPEND = !this
            }
            setIconVisible()
        } else {
            SHOWCLOSE = false
            SHOWEXPEND = true
            setIconVisible()
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener!!.onFocusChange(view, hasFocus)
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    /**
     * [TextWatcher]
     */

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused) {
            SHOWCLOSE = s.isNotEmpty() && count != 0
            SHOWEXPEND = true
            setIconVisible()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(context: Context, attrs: AttributeSet? = null) {

        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_close_black_24dp)

        val wrappedDrawable = DrawableCompat.wrap(drawable!!)

        //Wrap the drawable so that it can be tinted pre Lollipop
        //DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());

        mClearIcon = wrappedDrawable
        mClearIcon!!.setBounds(0, 0, mClearIcon!!.intrinsicWidth, mClearIcon!!.intrinsicHeight)

        mExpendLessIcon = ContextCompat.getDrawable(context, R.drawable.ic_expand_less_black_24dp)?.let {
            DrawableCompat.wrap(
                it
            ).apply {
                this.setBounds(0, 0, this!!.intrinsicWidth, this.intrinsicHeight)
            }
        }
        mExpendMoreIcon = ContextCompat.getDrawable(context, R.drawable.ic_expand_more_black_24dp)?.let {
            DrawableCompat.wrap(
                it
            ).apply {
                this.setBounds(0, 0, this!!.intrinsicWidth, this.intrinsicHeight)
            }
        }
        setIconVisible()
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        super.addTextChangedListener(this)

        ViewCompat.setBackgroundTintList(this, ContextCompat.getColorStateList(context, R.color.black60))
    }

    private fun setIconVisible() {
        val Tem: Drawable? = if (!SHOWCLOSE) {//未聚焦
            if (SHOWEXPEND) mExpendMoreIcon else mExpendLessIcon
        } else {//聚焦
            SHOWEXPEND = false
            mClearIcon
        }
        mClearIcon!!.setVisible(SHOWCLOSE, false)
        mExpendMoreIcon!!.setVisible(SHOWEXPEND, false)
        mExpendLessIcon!!.setVisible(!SHOWEXPEND, false)
        val compoundDrawables = compoundDrawables//left right top bottom
        //compoundDrawablesRelative // start end top bottom
        setCompoundDrawables(
            if (compoundDrawablesRelative[0] != null) compoundDrawablesRelative[0]
            else compoundDrawables[0],
            compoundDrawables[1],
            Tem,
            compoundDrawables[3]
        )

    }

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs)
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: View.OnFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: View.OnTouchListener) {
        mOnTouchListener = onTouchListener
    }

}

