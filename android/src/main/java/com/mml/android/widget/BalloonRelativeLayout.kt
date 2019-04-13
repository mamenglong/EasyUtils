package com.mml.android.widget

import android.animation.*
import android.content.Context
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.mml.android.R
import java.util.*

/**
 * Created by zhuyong on 2017/7/19.
 */

class BalloonRelativeLayout @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(mContext, attrs, defStyleAttr) {
    private var interpolators: MutableList<Interpolator>? = null//插值器数组
    private val linearInterpolator = LinearInterpolator()// 以常量速率改变
    private val accelerateInterpolator = AccelerateInterpolator()//加速
    private val decelerateInterpolator = DecelerateInterpolator()//减速
    private val accelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()//先加速后减速
    private var layoutParams: RelativeLayout.LayoutParams? = null
    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private val random = Random()

    //初始化随机数类

    private val mViewHeight = dip2px(context, 50f)

    //默认50dp
    private var drawables: MutableList<Drawable>? = null


    /**
     * 自定义曲线的两个控制点，随机在ViewGroup上的任何一个位置
     */
    private val pointF: PointF
        get() {
            val pointF = PointF()
            pointF.x = random.nextInt(mWidth).toFloat()
            pointF.y = random.nextInt(mHeight).toFloat()
            return pointF
        }

    init {
        init()
    }

    private fun init() {
        //初始化显示的图片
        drawables = mutableListOf()
        val mBalloon = ContextCompat.getDrawable(mContext, R.mipmap.balloon_pink)
        val mBalloon2 = ContextCompat.getDrawable(mContext, R.mipmap.balloon_purple)
        val mBalloon3 = ContextCompat.getDrawable(mContext, R.mipmap.balloon_blue)
        drawables!!.add(mBalloon!!)
        drawables!!.add(mBalloon2!!)
        drawables!!.add(mBalloon3!!)
        //设置view宽高相等，默认都是50dp
        layoutParams = RelativeLayout.LayoutParams(mViewHeight, mViewHeight)
        layoutParams!!.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)

        // 初始化插值器
        interpolators = mutableListOf()
        interpolators?.add(linearInterpolator)
        interpolators?.add(accelerateInterpolator)
        interpolators?.add(decelerateInterpolator)
        interpolators?.add(accelerateDecelerateInterpolator)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
    }

    /**
     * 调用此方法进行产出气泡
     */
    fun addBalloon() {
        val imageView = ImageView(context)
        //随机选一个
        imageView.setImageDrawable(drawables!![random.nextInt(3)])
        imageView.layoutParams = layoutParams
        addView(imageView)

        val animator = getAnimator(imageView)
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                //view动画结束后remove掉
                removeView(imageView)
            }
        })
        animator.start()
    }


    private fun getAnimator(target: View): Animator {

        val bezierValueAnimator = getBezierValueAnimator(target)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(bezierValueAnimator)
        animatorSet.interpolator = interpolators!![random.nextInt(4)]
        animatorSet.setTarget(target)
        return animatorSet
    }

    private fun getBezierValueAnimator(target: View): ValueAnimator {

        //初始化一个自定义的贝塞尔曲线插值器，并且传入控制点
        val evaluator = BezierEvaluator(pointF, pointF)

        //传入了曲线起点（左下角）和终点（顶部随机）
        val animator = ValueAnimator.ofObject(
            evaluator,
            PointF(0f, height.toFloat()),
            PointF(random.nextInt(width).toFloat(), (-mViewHeight).toFloat())
        )
        animator.addUpdateListener { animation ->
            //获取到贝塞尔曲线轨迹上的x和y值 赋值给view
            val pointF = animation.animatedValue as PointF
            target.x = pointF.x
            target.y = pointF.y
        }
        animator.setTarget(target)
        animator.duration = 5000
        return animator
    }


    /**
     * 自定义插值器
     */

    internal inner class BezierEvaluator(//途径的两个点
        private val pointF1: PointF, private val pointF2: PointF
    ) : TypeEvaluator<PointF> {

        override fun evaluate(
            time: Float, startValue: PointF,
            endValue: PointF
        ): PointF {

            val timeOn = 1.0f - time
            val point = PointF()
            //这么复杂的公式让我计算真心头疼，但是计算机很easy
            point.x = (timeOn * timeOn * timeOn * startValue.x
                    + 3f * timeOn * timeOn * time * pointF1.x
                    + 3f * timeOn * time * time * pointF2.x
                    + time * time * time * endValue.x)

            point.y = (timeOn * timeOn * timeOn * startValue.y
                    + 3f * timeOn * timeOn * time * pointF1.y
                    + 3f * timeOn * time * time * pointF2.y
                    + time * time * time * endValue.y)
            return point
        }
    }

    companion object {

        /**
         * Dip into pixels
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}
