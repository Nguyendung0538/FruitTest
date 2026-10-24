package com.example.fruittest.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtil {
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm"

    fun today(): String {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        return sdf.format(Date())
    }

    fun now(): String {
        val sdf = SimpleDateFormat(DATETIME_FORMAT, Locale.US)
        return sdf.format(Date())
    }

    fun isExpired(expiryDate: String): Boolean {
        if (expiryDate.isBlank()) return false
        return expiryDate < today()
    }

    fun isExpiringSoon(expiryDate: String): Boolean {
        if (expiryDate.isBlank()) return false
        val todayStr = today()
        if (expiryDate < todayStr) return false

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 3)
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
        val in3DaysStr = sdf.format(cal.time)

        return expiryDate <= in3DaysStr
    }
}
