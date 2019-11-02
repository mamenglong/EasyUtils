package com.mml.android.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi

/**
 * 项目名称：Library
 * @author Long
 * @date 2018/7/1
 * 修改时间：2018/7/1 23:59
 * desc   : 简单的Layout
 * （常用于自定义组合控件继承的基类，可以起到性能优化的作用）
 */
class SimpleLayout : ViewGroup {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var maxHeight = 0
        var maxWidth = 0
        var childState = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
                val lp = child.layoutParams as ViewGroup.MarginLayoutParams
                maxWidth = Math.max(maxWidth, child.measuredWidth + lp.leftMargin + lp.rightMargin)
                maxHeight = Math.max(maxHeight, child.measuredHeight + lp.topMargin + lp.bottomMargin)
                childState = View.combineMeasuredStates(childState, child.measuredState)
            }
        }

        maxWidth += paddingLeft + paddingRight
        maxHeight += paddingTop + paddingBottom

        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)
        maxWidth = Math.max(maxWidth, suggestedMinimumWidth)

        setMeasuredDimension(
            View.resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            View.resolveSizeAndState(
                maxHeight, heightMeasureSpec,
                childState shl View.MEASURED_HEIGHT_STATE_SHIFT
            )
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val params = child.layoutParams as ViewGroup.MarginLayoutParams
            child.layout(
                paddingLeft + params.leftMargin,
                paddingTop + params.topMargin,
                paddingRight + child.measuredWidth + params.rightMargin,
                paddingBottom + child.measuredHeight + params.bottomMargin
            )
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(p)
    }
}