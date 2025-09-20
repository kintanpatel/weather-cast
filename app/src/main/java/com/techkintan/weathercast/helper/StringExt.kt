package com.techkintan.weathercast.helper

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toDisplayDate(): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val formatter = SimpleDateFormat("EEEE dd MMM yyyy", Locale.ENGLISH)
        val date = parser.parse(this)
        formatter.format(date!!)
    } catch (e: Exception) {
        this
    }
}


fun String.toDisplayTime(): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val date = parser.parse(this)
        formatter.format(date!!)
    } catch (e: Exception) {
        this
    }
}
