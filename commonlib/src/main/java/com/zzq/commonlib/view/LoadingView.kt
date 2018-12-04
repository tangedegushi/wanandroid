package com.zzq.commonlib.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.zzq.commonlib.R

/**
 *@auther tangedegushi
 *@creat 2018/11/30
 *@Decribe
 */
class LoadingView(context: Context,var attrs: AttributeSet?, defStyleAttr: Int): View(context,attrs,defStyleAttr) {

    //默认值
    private val RADIUS = dp2px(6f)
    private val GAP = dp2px(0.8f)
    private val RTL_SCALE = 0.7f
    private val LTR_SCALR = 1.3f
    private val LEFT_COLOR = -0xbfc0
    private val RIGHT_COLOR = -0xff1112
    private val MIX_COLOR = Color.BLACK
    private val DURATION = 350
    private val PAUSE_DUARTION = 80
    private val SCALE_START_FRACTION = 0.2f
    private val SCALE_END_FRACTION = 0.8f

    //属性
    private var radius1: Float = 0.toFloat() //初始时左小球半径
    private var radius2: Float = 0.toFloat() //初始时右小球半径
    private var gap: Float = 0.toFloat() //两小球直接的间隔
    private var rtlScale: Float = 0.toFloat() //小球从右边移动到左边时大小倍数变化(rtl = right to left)
    private var ltrScale: Float = 0.toFloat()//小球从左边移动到右边时大小倍数变化
    private var color1: Int = 0//初始左小球颜色
    private var color2: Int = 0//初始右小球颜色
    private var mixColor: Int = 0//两小球重叠处的颜色
    private var duration: Int = 0 //小球一次移动时长
    private var pauseDuration: Int = 0//小球一次移动后停顿时长
    private var scaleStartFraction: Float = 0.toFloat() //小球一次移动期间，进度在[0,scaleStartFraction]期间根据rtlScale、ltrScale逐渐缩放，取值为[0,0.5]
    private var scaleEndFraction: Float = 0.toFloat()//小球一次移动期间，进度在[scaleEndFraction,1]期间逐渐恢复初始大小,取值为[0.5,1]
    private var start: Boolean = false//是否一开始就开始动画

    //绘图
    private var paint1: Paint? = null
    private var paint2:Paint? = null
    private var mixPaint:Paint? = null
    private var ltrPath: Path? = null
    private var rtlPath:Path? = null
    private var mixPath:Path? = null
    private var distance: Float = 0.toFloat() //小球一次移动距离(即两球圆点之间距离）

    //动画
    private var anim: ValueAnimator? = null
    private var fraction: Float = 0.toFloat() //小球一次移动动画的进度百分比
    internal var isAnimCanceled = false
    internal var isLtr = true//true = 【初始左球】当前正【从左往右】移动,false = 【初始左球】当前正【从右往左】移动

    constructor(context: Context):this(context, null)

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView)
        radius1 = ta.getDimension(R.styleable.LoadingView_radius1, RADIUS)
        radius2 = ta.getDimension(R.styleable.LoadingView_radius2, RADIUS)
        gap = ta.getDimension(R.styleable.LoadingView_gap, GAP)
        rtlScale = ta.getFloat(R.styleable.LoadingView_rtlScale, RTL_SCALE)
        ltrScale = ta.getFloat(R.styleable.LoadingView_ltrScale, LTR_SCALR)
        color1 = ta.getColor(R.styleable.LoadingView_color1, LEFT_COLOR)
        color2 = ta.getColor(R.styleable.LoadingView_color2, RIGHT_COLOR)
        mixColor = ta.getColor(R.styleable.LoadingView_mixColor, MIX_COLOR)
        duration = ta.getInt(R.styleable.LoadingView_duration, DURATION)
        pauseDuration = ta.getInt(R.styleable.LoadingView_pauseDuration, PAUSE_DUARTION)
        scaleStartFraction = ta.getFloat(R.styleable.LoadingView_scaleStartFraction, SCALE_START_FRACTION)
        scaleEndFraction = ta.getFloat(R.styleable.LoadingView_scaleEndFraction, SCALE_END_FRACTION)
        start = ta.getBoolean(R.styleable.LoadingView_start, false)
        distance = gap + radius1 + radius2
        ta.recycle()
        checkAttr()
        initDrawParams()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var wSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val hMode = View.MeasureSpec.getMode(heightMeasureSpec)
        var hSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var maxScale = Math.max(ltrScale, rtlScale)
        maxScale = Math.max(maxScale, 1f)
        if (wMode != View.MeasureSpec.EXACTLY) {
            wSize = (gap + 2f * (radius1 + radius2) * maxScale + dp2px(1f)).toInt()
        }
        if (hMode != View.MeasureSpec.EXACTLY) {
            hSize = (2f * Math.max(radius1, radius2) * maxScale + dp2px(1f)).toInt()
        }
        Log.i("LodingView","width = " + wSize + "   height = " + hSize + "   mode = " + (wMode shr 30) + "  " + (hMode shr 30))
        setMeasuredDimension(wSize, hSize)
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    /**
     * 属性合法性检查校正
     */
    private fun checkAttr() {
        radius1 = if (radius1 > 0) radius1 else RADIUS
        radius2 = if (radius2 > 0) radius2 else RADIUS
        gap = if (gap >= 0) gap else GAP
        rtlScale = if (rtlScale >= 0) rtlScale else RTL_SCALE
        ltrScale = if (ltrScale >= 0) ltrScale else LTR_SCALR
        duration = if (duration > 0) duration else DURATION
        pauseDuration = if (pauseDuration >= 0) pauseDuration else PAUSE_DUARTION
        if (scaleStartFraction < 0 || scaleStartFraction > 0.5f) {
            scaleStartFraction = SCALE_START_FRACTION
        }
        if (scaleEndFraction < 0.5 || scaleEndFraction > 1) {
            scaleEndFraction = SCALE_END_FRACTION
        }
    }

    fun initDrawParams() {
        paint1 = Paint()
        paint2 = Paint()
        mixPaint = Paint()

        paint1!!.isAntiAlias = true
        paint1!!.color = color1
        paint2!!.setAntiAlias(true)
        paint2!!.setColor(color2)
        mixPaint!!.setAntiAlias(true)
        mixPaint!!.setColor(mixColor)

        ltrPath = Path()
        rtlPath = Path()
        mixPath = Path()
    }

    private fun initAnim() {
        fraction = 0.0f

        stop()

        anim = ValueAnimator.ofFloat(0.0f, 1.0f)
        anim!!.duration = duration.toLong()
        if (pauseDuration > 0) {
            anim!!.startDelay = pauseDuration.toLong()
            anim!!.interpolator = AccelerateDecelerateInterpolator()
        } else {
            anim!!.repeatCount = ValueAnimator.INFINITE
            anim!!.repeatMode = ValueAnimator.RESTART
            anim!!.interpolator = LinearInterpolator()
        }

        anim!!.addUpdateListener { animation ->
            fraction = animation.animatedFraction
            invalidate()
        }
        anim!!.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                isLtr = !isLtr
            }

            override fun onAnimationCancel(animation: Animator) {
                isAnimCanceled = true
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!isAnimCanceled) {
                    anim?.start()
                }
            }
        })

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerY = measuredHeight / 2.0f

        val ltrInitRadius: Float
        val rtlInitRadius: Float
        val ltrPaint: Paint?
        val rtlPaint: Paint?

        //确定当前【从左往右】移动的是哪颗小球
        if (isLtr) {
            ltrInitRadius = radius1
            rtlInitRadius = radius2
            ltrPaint = paint1
            rtlPaint = paint2
        } else {
            ltrInitRadius = radius2
            rtlInitRadius = radius1
            ltrPaint = paint2
            rtlPaint = paint1
        }


        var ltrX = (measuredWidth - distance) / 2.0f
        ltrX = ltrX + distance * fraction//当前从左往右的球的X坐标

        var rtlX = (measuredWidth + distance) / 2.0f
        rtlX = rtlX - distance * fraction//当前从右往左的球的X坐标

        //计算小球移动过程中的大小变化
        val ltrBallRadius: Float
        val rtlBallRadius: Float
        if (fraction <= scaleStartFraction) { //动画进度[0,scaleStartFraction]时，球大小由1倍逐渐缩放至ltrScale/rtlScale倍
            val scaleFraction = 1.0f / scaleStartFraction * fraction //百分比转换 [0,scaleStartFraction]] -> [0,1]
            ltrBallRadius = ltrInitRadius * (1 + (ltrScale - 1) * scaleFraction)
            rtlBallRadius = rtlInitRadius * (1 + (rtlScale - 1) * scaleFraction)
        } else if (fraction >= scaleEndFraction) { //动画进度[scaleEndFraction,1]，球大小由ltrScale/rtlScale倍逐渐恢复至1倍
            val scaleFraction = (1 - fraction) / (1 - scaleEndFraction) //百分比转换，[scaleEndFraction,1] -> [1,0]
            ltrBallRadius = ltrInitRadius * (1 + (ltrScale - 1) * scaleFraction)
            rtlBallRadius = rtlInitRadius * (1 + (rtlScale - 1) * scaleFraction)
        } else { //动画进度[scaleStartFraction,scaleEndFraction]，球保持缩放后的大小
            ltrBallRadius = ltrInitRadius * ltrScale
            rtlBallRadius = rtlInitRadius * rtlScale
        }

        ltrPath!!.reset()
        ltrPath!!.addCircle(ltrX, centerY, ltrBallRadius, Path.Direction.CW)
        rtlPath!!.reset()
        rtlPath!!.addCircle(rtlX, centerY, rtlBallRadius, Path.Direction.CW)
        mixPath!!.op(ltrPath, rtlPath, Path.Op.INTERSECT)

        canvas.drawPath(ltrPath!!, ltrPaint!!)
        canvas.drawPath(rtlPath!!, rtlPaint!!)
        canvas.drawPath(mixPath!!, mixPaint!!)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (start) {
            start()
        }
    }

    override fun onDetachedFromWindow() {
        stop()
        super.onDetachedFromWindow()
    }

    /**
     * 停止动画
     */
    fun stop() {
        if (anim != null) {
            anim!!.cancel()
            anim = null
        }
    }

    /**
     * 开始动画
     */
    fun start() {
        if (anim == null) {
            initAnim()
        }
        if (anim!!.isRunning) {
            anim!!.cancel()
        }

        post {
            isAnimCanceled = false
            isLtr = false
            if (anim != null)
                anim!!.start()
        }
    }

    /**
     * 设置小球半径和两小球间隔
     */
    fun setRadius(radius1: Float, radius2: Float, gap: Float) {
        stop()
        this.radius1 = radius1
        this.radius2 = radius2
        this.gap = gap
        checkAttr()
        distance = gap + radius1 + radius2
        requestLayout() //可能涉及宽高变化
    }


    /**
     * 设置小球颜色和重叠处颜色
     */
    fun setColors(color1: Int, color2: Int, mixColor: Int) {
        this.color1 = color1
        this.color2 = color2
        this.mixColor = color2
        checkAttr()
        paint1!!.color = color1
        paint2!!.setColor(color2)
        mixPaint!!.setColor(mixColor)
        invalidate()
    }

    /**
     * 设置动画时长
     *
     * @param duration      [.duration]
     * @param pauseDuration [.pauseDuration]
     */
    fun setDuration(duration: Int, pauseDuration: Int) {
        this.duration = duration
        this.pauseDuration = pauseDuration
        checkAttr()
        initAnim()
    }

    /**
     * 设置移动过程中缩放倍数
     *
     * @param ltrScale [.ltrScale]
     * @param rtlScale [.rtlScale]
     */
    fun setScales(ltrScale: Float, rtlScale: Float) {
        stop()
        this.ltrScale = ltrScale
        this.rtlScale = rtlScale
        checkAttr()
        requestLayout() //可能涉及宽高变化
    }

    /**
     * 设置缩放开始、结束的范围
     *
     * @param scaleStartFraction [.scaleStartFraction]
     * @param scaleEndFraction   [.scaleEndFraction]
     */
    fun setStartEndFraction(scaleStartFraction: Float, scaleEndFraction: Float) {
        this.scaleStartFraction = scaleStartFraction
        this.scaleEndFraction = scaleEndFraction
        checkAttr()
        invalidate()
    }


    fun getRadius1(): Float {
        return radius1
    }

    fun getRadius2(): Float {
        return radius2
    }

    fun getGap(): Float {
        return gap
    }

    fun getRtlScale(): Float {
        return rtlScale
    }

    fun getLtrScale(): Float {
        return ltrScale
    }

    fun getColor1(): Int {
        return color1
    }

    fun getColor2(): Int {
        return color2
    }

    fun getMixColor(): Int {
        return mixColor
    }

    fun getDuration(): Int {
        return duration
    }

    fun getPauseDuration(): Int {
        return pauseDuration
    }

    fun getScaleStartFraction(): Float {
        return scaleStartFraction
    }

    fun getScaleEndFraction(): Float {
        return scaleEndFraction
    }

}