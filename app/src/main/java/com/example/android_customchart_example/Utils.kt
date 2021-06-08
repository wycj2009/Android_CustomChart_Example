package com.example.android_customchart_example

import android.content.Context
import android.text.format.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {

    fun getDateWithBestPattern(localDate: LocalDate, pattern: String): String {
        val bestDateTimePattern: String = DateFormat.getBestDateTimePattern(Locale.getDefault(), pattern)
        return localDate.format(DateTimeFormatter.ofPattern(bestDateTimePattern))
    }

    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

}