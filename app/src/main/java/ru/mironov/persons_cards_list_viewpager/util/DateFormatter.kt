package ru.mironov.persons_cards_list_viewpager.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    private const val UI_DATE_FORMAT = "dd MMM"
    private const val API_DATE_FORMAT = "yyyy-MM-dd"

    @SuppressLint("SimpleDateFormat")
    fun convertDate(date: String): String {
        val date = SimpleDateFormat(API_DATE_FORMAT).parse(date)
        return SimpleDateFormat(UI_DATE_FORMAT, Locale.getDefault()).format(date)
    }
}