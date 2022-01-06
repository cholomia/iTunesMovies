package com.pocholomia.itunes.ui.utils

import android.content.Context
import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*


fun Date.toReadableString(dateTimeFormat: String): String? {
    val simpleDateFormat = SimpleDateFormat(dateTimeFormat, Locale.getDefault())
    return simpleDateFormat.format(this)
}

fun String.toDate(dateTimeFormat: String): Date? = try {
    val simpleDateFormat = SimpleDateFormat(dateTimeFormat, Locale.getDefault())
    val date = this.replace("Z", "+00:00")
    simpleDateFormat.parse(date)
} catch (e: Throwable) {
    e.printStackTrace()
    null
}