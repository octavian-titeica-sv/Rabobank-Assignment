package com.example.csvreader.base.util

import android.util.Log
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

const val INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

fun convertStringToDate(dateAsString: String) : String {
    return try {
        val inputDateFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
        val date = inputDateFormat.parse(dateAsString)
        return date?.let { nonNullDate -> DateFormat.getDateInstance(DateFormat.MEDIUM).format(nonNullDate) } ?: EMPTY_STRING
    } catch (error: Throwable) {
        Log.e("DateConverter", "error converting date: ${error.message}")
        EMPTY_STRING
    }
}
