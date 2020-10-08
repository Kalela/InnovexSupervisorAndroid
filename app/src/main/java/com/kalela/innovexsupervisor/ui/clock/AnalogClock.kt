package com.kalela.innovexsupervisor.ui.clock

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class AnalogClock @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private val TAG = "AnalogClock"

    private var mHeight = 0
    private var mWidth = 0
    private var mRadius = 0
    private var mAngle = 0.0
    private var mCentreX = 0
    private var mCentreY = 0
    private var mPadding = 0
    private var mIsInit = false
    private var mPaint: Paint? = null
    private var mPath: Path? = null
    private lateinit var mNumbers: IntArray
    private lateinit var mRect: Rect
    private var mMinimum = 0
    private var mHour = 0
    private var mMinute = 0
    private var mSecond = 0
    private var mHourHandSize = 0
    private var mHandSize = 0
    private val mFontSize = 50.0f

    private fun init() {
        mHeight = height
        mWidth = width
        mPadding = 50
        mCentreX = mWidth / 2
        mCentreY = mHeight / 2
        mMinimum = min(mHeight, mWidth)
        mRadius = mMinimum / 2 - mPadding
        mAngle = (Math.PI / 30 - Math.PI / 2) as Double
        mPaint = Paint()
        mPath = Path()
        mRect = Rect()
        mHourHandSize = mRadius - mRadius / 2
        mHandSize = mRadius - mRadius / 4
        mNumbers = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        mIsInit = true

    }

    private fun initializeClock() {
        Timer().scheduleAtFixedRate(timerTask {
            mSecond += 1
            if(mSecond % 60 == 0) {
                mMinute += 1
            }
        }, 1000, 1000)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!mIsInit) {
            init()
            initializeClock()
        }

        drawCircle(canvas)
        drawHands(canvas)
        drawNumerals(canvas)
        postInvalidateDelayed(500)
    }

    private fun drawCircle(canvas: Canvas?) {
        mPaint?.reset()
        setPaintAttributes(Color.BLACK, Paint.Style.FILL, 8.0f)
        mPaint?.let {
            canvas?.drawCircle(
                mCentreX.toFloat(), mCentreY.toFloat(), mRadius.toFloat(),
                it
            )
        }
    }

    private fun setPaintAttributes(color: Int, stroke: Paint.Style, strokeWidth: Float) {
        mPaint?.reset()
        mPaint?.color = color
        mPaint?.style = stroke
        mPaint?.strokeWidth = strokeWidth
        mPaint?.isAntiAlias = true
    }

    private fun drawHands(canvas: Canvas?) {
        drawHourHand(canvas, (mHour + mMinute / 60.0) * 5f)
        drawMinuteHand(canvas, mMinute * 1f)
        drawSecondsHand(canvas, mSecond.toFloat())
    }

    private fun drawSecondsHand(canvas: Canvas?, location: Float) {
        mPaint?.reset()
        setPaintAttributes(Color.RED, Paint.Style.STROKE, 8.0f)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        mPaint?.let {
            canvas?.drawLine(
                mCentreX.toFloat(),
                mCentreY.toFloat(),
                (mCentreX + cos(mAngle) * mHandSize).toFloat()
                ,
                (mCentreY + sin(mAngle) * mHourHandSize).toFloat(),
                it
            )
        }
    }

    private fun drawMinuteHand(canvas: Canvas?, location: Float) {
        mPaint?.reset()
        setPaintAttributes(Color.WHITE, Paint.Style.STROKE, 8.0f)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        mPaint?.let {
            canvas?.drawLine(
                mCentreX.toFloat(),
                mCentreY.toFloat(),
                (mCentreX + cos(mAngle) * mHandSize).toFloat()
                ,
                (mCentreY + sin(mAngle) * mHourHandSize).toFloat(),
                it
            )
        }
    }

    private fun drawHourHand(canvas: Canvas?, location: Double) {
        mPaint?.reset()
        setPaintAttributes(Color.WHITE, Paint.Style.STROKE, 10.0f)
        mAngle = Math.PI * location / 30 - Math.PI / 2
        mPaint?.let {
            canvas?.drawLine(
                mCentreX.toFloat(),
                mCentreY.toFloat(),
                (mCentreX + cos(mAngle) * mHourHandSize).toFloat()
                ,
                (mCentreY + sin(mAngle) * mHourHandSize).toFloat(),
                it
            )
        }
    }

    private fun drawNumerals(canvas: Canvas?) {
        mPaint?.textSize = mFontSize
        for (number: Int in mNumbers) {
            val num: String = number.toString()
            mPaint?.getTextBounds(num, 0, num.length, mRect)
            val angle: Double = Math.PI / 6 * (number - 3)
            val x: Float = (mCentreX + cos(angle) * mRadius - mRect.width() / 2).toFloat()
            val y: Float = (mCentreY + sin(angle) * mRadius + mRect.height() / 2).toFloat()
            mPaint?.let { canvas?.drawText(num, x, y, it) }
        }
    }
}