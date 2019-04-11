package com.mml.android.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.mml.android.R

/**
 * 项目名称：Library
 * @author Long
 * @date 2018/7/1
 * 修改时间：2018/7/1 23:59
 * desc   : 带清除按钮的EditText
 */
class ClearEditText : AppCompatEditText, View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private var mClearIcon: Drawable? = null

    private var mOnTouchListener: View.OnTouchListener? = null
    private var mOnFocusChangeListener: View.OnFocusChangeListener? = null

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) {

        val drawable = ContextCompat.getDrawable(context, R.mipmap.icon_input_del)

        val wrappedDrawable = DrawableCompat.wrap(drawable!!)

        //Wrap the drawable so that it can be tinted pre Lollipop
        //DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());

        mClearIcon = wrappedDrawable
        mClearIcon!!.setBounds(0, 0, mClearIcon!!.intrinsicWidth, mClearIcon!!.intrinsicHeight)
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        super.addTextChangedListener(this)
        ViewCompat.setBackgroundTintList(this, ContextCompat.getColorStateList(context, R.color.black60))
    }

    private fun setClearIconVisible(visible: Boolean) {
        if (mClearIcon!!.isVisible == visible) return

        mClearIcon!!.setVisible(visible, false)
        val compoundDrawables = compoundDrawables
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            if (visible) mClearIcon else null,
            compoundDrawables[3]
        )
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: View.OnFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: View.OnTouchListener) {
        mOnTouchListener = onTouchListener
    }

    /**
     * [OnFocusChangeListener]
     */

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus && text != null) {
            setClearIconVisible(text!!.length > 0)
        } else {
            setClearIconVisible(false)
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener!!.onFocusChange(view, hasFocus)
        }
    }

    /**
     * [OnTouchListener]
     */

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x.toInt()
        if (mClearIcon!!.isVisible && x > width - paddingRight - mClearIcon!!.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                setText("")
            }
            return true
        }
        return mOnTouchListener != null && mOnTouchListener!!.onTouch(view, motionEvent)
    }

    /**
     * [TextWatcher]
     */

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused) {
            setClearIconVisible(s.length > 0)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {}

    //    @Override
    //    protected void drawableStateChanged() {
    //        super.drawableStateChanged();
    //    }
}