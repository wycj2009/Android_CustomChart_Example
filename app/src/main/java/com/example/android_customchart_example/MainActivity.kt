package com.example.android_customchart_example

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBarchartChart: BarChart
    private lateinit var activityMainLinechartChart: LineChart
    private lateinit var activityMainButtonTest1: Button
    private lateinit var activityMainButtonTest2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityMainBarchartChart = findViewById(R.id.activity_main_barchart_chart)
        activityMainLinechartChart = findViewById(R.id.activity_main_linechart_chart)
        activityMainButtonTest1 = findViewById(R.id.activity_main_button_test_1)
        activityMainButtonTest2 = findViewById(R.id.activity_main_button_test_2)

        activityMainButtonTest1.setOnClickListener {
            activityMainBarchartChart.visibility = View.VISIBLE
            activityMainLinechartChart.visibility = View.INVISIBLE
            activityMainBarchartChart.removeAllViews()
            activityMainLinechartChart.removeAllViews()

            val dataList = arrayListOf(
                    Pair(LocalDate.of(2020, 12, 1), 40.0f),
                    Pair(LocalDate.of(2020, 12, 2), 80.0f),
                    Pair(LocalDate.of(2020, 12, 3), 10.0f),
                    Pair(LocalDate.of(2020, 12, 4), 20.0f),
                    Pair(LocalDate.of(2020, 12, 5), 90.0f),
                    Pair(LocalDate.of(2020, 12, 6), 60.0f),
                    Pair(LocalDate.of(2020, 12, 7), 100.0f),
                    Pair(LocalDate.of(2020, 12, 8), 50.0f),
                    Pair(LocalDate.of(2020, 12, 9), 30.0f),
                    Pair(LocalDate.of(2020, 12, 10), 70.0f)
            )
            activityMainBarchartChart.createChart(5, 0, 100, dataList, ContextCompat.getColor(this, R.color.white))//차트 생성
        }

        activityMainButtonTest2.setOnClickListener {
            activityMainBarchartChart.visibility = View.INVISIBLE
            activityMainLinechartChart.visibility = View.VISIBLE
            activityMainBarchartChart.removeAllViews()
            activityMainLinechartChart.removeAllViews()

            val dataList = arrayListOf(
                    Pair(LocalDate.of(2020, 12, 1), 40.0f),
                    Pair(LocalDate.of(2020, 12, 2), 80.0f),
                    Pair(LocalDate.of(2020, 12, 3), 10.0f),
                    Pair(LocalDate.of(2020, 12, 4), 20.0f),
                    Pair(LocalDate.of(2020, 12, 5), 90.0f),
                    Pair(LocalDate.of(2020, 12, 6), 60.0f),
                    Pair(LocalDate.of(2020, 12, 7), 100.0f),
                    Pair(LocalDate.of(2020, 12, 8), 50.0f),
                    Pair(LocalDate.of(2020, 12, 9), 30.0f),
                    Pair(LocalDate.of(2020, 12, 10), 70.0f)
            )
            activityMainLinechartChart.createChart(5, 0, 100, dataList, ContextCompat.getColor(this, R.color.white))
        }
    }

}