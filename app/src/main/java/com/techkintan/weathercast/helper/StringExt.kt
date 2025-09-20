package com.techkintan.weathercast.helper

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


fun String.toIndianDateFormatted(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(this)

        val outputFormat = SimpleDateFormat("dd MMM yyyy, EEE", Locale("en", "IN"))
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

        outputFormat.format(date!!)
    } catch (e: Exception) {
        this // fallback to original string if parsing fails
    }
}

