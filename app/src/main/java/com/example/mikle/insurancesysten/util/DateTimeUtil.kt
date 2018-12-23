package com.example.mikle.insurancesysten.util

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    private val DATE_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"

    fun getDateTime(): String {
        return format(Date(), DATE_YYYY_MM_DD_HH_MM_SS)
    }

    private fun format(date: Date, template: String): String {
        try {
            return format(template).format(date)
        } catch (e: NumberFormatException) {
            Log.e(javaClass.simpleName, e.message)
        }


        return ""
    }

    private fun format(template: String): SimpleDateFormat {
        return SimpleDateFormat(template, Locale.getDefault())
    }
}