package com.ldl.materialdemo

import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator

/**
 * @author LDL
 * @date: 2022/11/1
 * @description:
 */
class PathMeasureView(context: Context, attributeSet: AttributeSet?) :
    View(context, attributeSet) {
    private val mPath: Path
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPathMeasure: PathMeasure
    private val mDst: Path
    private val mLength: Float
    private var mAnimatorValue: Float = 0f
    private val mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTextRect = Rect()

    private val mStrokeWidth = 10f
    private val size = 100f
    private val radius = 20f

    init {
        mPaint.apply {
            style = Paint.Style.STROKE
            color = Color.RED
            strokeWidth = mStrokeWidth
            isAntiAlias = true
        }
        mTextPaint.apply {
            color = Color.RED
            textSize = 32f
            isAntiAlias = true
        }
        mPath = Path()
        mPath.moveTo(size / 2, 0f)
        mPath.rLineTo((size - 2 * radius) / 2, 0f)
        mPath.arcTo(size - radius, 0f, size, radius, -90f, 90f, false)
        mPath.rLineTo(0f, size - radius)
        mPath.arcTo(size - radius, size - radius, size, size, 0f, 90f, false)
        mPath.rLineTo(radius - size, 0f)
        mPath.arcTo(0f, size - radius, radius, size, 90f, 90f, false)
        mPath.rLineTo(0f, radius - size)
        mPath.arcTo(0f, 0f, radius, radius, -180f, 90f, false)
        mPath.close()
//        mPath.addRoundRect(100f, 100f, 300f, 300f, 50f, 50f, Path.Direction.CW)
        mPathMeasure = PathMeasure(mPath, false)
        mDst = Path()
        mLength = mPathMeasure.length

        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.apply {
            duration = 6 * 1000
            addUpdateListener { valueAnimator ->
                mAnimatorValue = valueAnimator.animatedValue as Float
                postInvalidate()
            }
            interpolator = DecelerateInterpolator()
            repeatCount = INFINITE
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(500, 500)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mDst.reset()
        canvas?.translate((500 - size) / 2, (500 - size) / 2)
        val start: Float
        val stop: Float
        if (mAnimatorValue <= 1) {
            start = 0f
            stop = mLength * mAnimatorValue
        } else {
            start = (mAnimatorValue - 1) * mLength
            stop = mLength
        }
        mPathMeasure.getSegment(start, stop, mDst, true)
        canvas?.drawPath(mDst, mPaint)
        val str = "${(mAnimatorValue * 100).toInt()}%"
        mTextPaint.getTextBounds(str, 0, str.length, mTextRect)
        canvas?.drawText(
            "${(mAnimatorValue * 100).toInt()}%",
            (size - mTextRect.width()) / 2,
            (size + mTextRect.height()) / 2,
            mTextPaint
        )
    }
}