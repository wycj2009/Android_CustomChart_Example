package com.example.android_customchart_example

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import java.time.LocalDate
import kotlin.math.absoluteValue

class LineChart : ConstraintLayout {

    private var startValue: Int = 0
    private var lastValue: Int = 0
    private lateinit var dataList: ArrayList<Pair<LocalDate, Float>>
    private var mainColor: Int = 0
    private lateinit var horizontalLineList: ArrayList<View>
    private lateinit var pointList: ArrayList<PointF>
    private lateinit var dataBox: TextView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    //차트 생성
    fun createChart(horizontalLineCount: Int, startValue: Int, lastValue: Int, dataList: List<Pair<LocalDate, Float>>, mainColor: Int) {
        this.startValue = startValue
        this.lastValue = lastValue
        this.dataList = ArrayList(dataList)
        this.mainColor = mainColor
        this.horizontalLineList = ArrayList()

        //데이터박스 세팅
        dataBox = TextView(context)
        dataBox.id = View.generateViewId()
        dataBox.width = Utils.pxFromDp(context, 50.0f).toInt()
        dataBox.height = Utils.pxFromDp(context, 25.0f).toInt()
        val paddingValue = Utils.pxFromDp(context, 2.0f).toInt()
        dataBox.setPadding(paddingValue, 0, paddingValue, 0)
        dataBox.gravity = Gravity.CENTER
        dataBox.background = ContextCompat.getDrawable(context, R.drawable.ic_data_box)
        dataBox.elevation = Utils.pxFromDp(context, 1.0f)
        dataBox.visibility = View.INVISIBLE
        this.addView(dataBox)

        //가로선 세팅
        for (i in 0..horizontalLineCount) {
            val line = View(context)
            horizontalLineList.add(line)
            line.id = View.generateViewId()
            val lineLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, Utils.pxFromDp(context, 1.5f).toInt())
            lineLayoutParams.rightMargin = Utils.pxFromDp(context, 50.0f).toInt()
            line.layoutParams = lineLayoutParams
            line.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            this.addView(line)

            val lineConstraintSet = ConstraintSet()
            lineConstraintSet.clone(this)
            lineConstraintSet.connect(line.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            lineConstraintSet.connect(line.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            lineConstraintSet.setVerticalBias(line.id, ((0.8f / horizontalLineCount * (horizontalLineCount - i)) + 0.1f))
            lineConstraintSet.applyTo(this)
        }

        //오른쪽 텍스트 세팅
        for (i in 0..horizontalLineCount) {
            val rightText = TextView(context)
            rightText.id = View.generateViewId()
            val rightTextLayoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT)
            rightTextLayoutParams.leftMargin = Utils.pxFromDp(context, 10.0f).toInt()
            rightText.layoutParams = rightTextLayoutParams
            rightText.text = "${((lastValue - startValue) / horizontalLineCount * i)}"
            rightText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0f)
            this.addView(rightText)

            val rightTextConstraintSet = ConstraintSet()
            rightTextConstraintSet.clone(this)
            rightTextConstraintSet.connect(rightText.id, ConstraintSet.TOP, horizontalLineList[i].id, ConstraintSet.TOP)
            rightTextConstraintSet.connect(rightText.id, ConstraintSet.BOTTOM, horizontalLineList[i].id, ConstraintSet.BOTTOM)
            rightTextConstraintSet.connect(rightText.id, ConstraintSet.LEFT, horizontalLineList[i].id, ConstraintSet.RIGHT)
            rightTextConstraintSet.connect(rightText.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            rightTextConstraintSet.applyTo(this)
        }

        drawChart()//차트 그리기
    }

    //차트 그리기
    private fun drawChart() {
        pointList = ArrayList<PointF>()
        val graphAreaWidth = this.width - Utils.pxFromDp(context, 50.0f)

        //데이터 라인 세팅
        for (i in 0..dataList.size - 1) {
            val pointYPx: Float = (0.9f * this.height) - (this.height * 0.8f * dataList[i].second / (lastValue - startValue))
            pointList.add(PointF((graphAreaWidth * (i.toFloat() + 0.5f) / dataList.size), pointYPx))
        }
        val line = Line(context)
        line.id = View.generateViewId()
        line.drawLine(pointList, mainColor)
        this.addView(line)

        //하단 텍스트 세팅
        for (i in 0..dataList.size - 1) {
            val bottomText = TextView(context)
            bottomText.id = View.generateViewId()
            val bottomTextLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            bottomText.layoutParams = bottomTextLayoutParams
            bottomText.text = "${Utils.getDateWithBestPattern(dataList[i].first, "MMdd")}"
            bottomText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0f)
            this.addView(bottomText)

            val bottomTextConstraintSet = ConstraintSet()
            bottomTextConstraintSet.clone(this)
            bottomTextConstraintSet.connect(bottomText.id, ConstraintSet.TOP, horizontalLineList[0].id, ConstraintSet.BOTTOM)
            bottomTextConstraintSet.connect(bottomText.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            bottomTextConstraintSet.connect(bottomText.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            bottomTextConstraintSet.applyTo(this)

            bottomText.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    bottomText.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    var x: Float = graphAreaWidth * (i.toFloat() + 0.5f) / dataList.size - (bottomText.width * 0.5f)
                    if (x < 0.0f) {
                        x = 0.0f
                    } else if (x + bottomText.width > graphAreaWidth) {
                        x = graphAreaWidth - bottomText.width.toFloat()
                    }
                    bottomText.x = x
                }
            })
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        if (event?.action == MotionEvent.ACTION_DOWN || event?.action == MotionEvent.ACTION_MOVE) {
            val distanceList = arrayListOf<Float>()
            for (point in pointList) {
                distanceList.add((event.x - point.x).absoluteValue)
            }
            val nearestPoint = pointList[distanceList.indexOf(distanceList.min())]
            dataBox.x = nearestPoint.x - (dataBox.width * 0.5f)
            dataBox.y = nearestPoint.y - dataBox.height.toFloat() - Utils.pxFromDp(context, 5.0f)
            dataBox.text = "data"
            dataBox.visibility = View.VISIBLE
        }

        return true
    }

    class Line : View {
        private val paint: Paint = Paint()
        private val path: Path = Path()
        private var progress: Float = 0.0f

        constructor(context: Context?) : super(context)
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        fun drawLine(pointList: ArrayList<PointF>, mainColor: Int) {
            paint.apply {
                color = mainColor
                style = Paint.Style.STROKE
                isAntiAlias = true
                strokeWidth = Utils.pxFromDp(context, 4.0f)
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
            }

            if (pointList.size > 0) {
                path.moveTo(pointList[0].x, pointList[0].y)
                for (point in pointList) {
                    path.lineTo(point.x, point.y)
                }
            }

            val animation = ObjectAnimator.ofFloat(this, "progress", 0.0f, 1.0f)
            animation.duration = 1000
            animation.start()
        }

        fun setProgress(progress: Float) {
            this.progress = progress
            invalidate()
        }

        override fun onDraw(canvas: Canvas?) {
            val pathLength = PathMeasure(path, false).length
            val total = pathLength - (pathLength * progress)
            val pathEffect = DashPathEffect(floatArrayOf(pathLength, pathLength), total)

            val cornerPathEffect = CornerPathEffect(0.0f)
            paint.pathEffect = ComposePathEffect(cornerPathEffect, pathEffect)

            canvas?.drawPath(path, paint)
        }
    }

}