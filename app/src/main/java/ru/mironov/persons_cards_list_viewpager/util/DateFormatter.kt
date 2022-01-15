package ru.mironov.persons_cards_list_viewpager.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    private const val UI_DATE_FORMAT = "dd MMM"
    private const val UI_DATE_FORMAT_FULL = "dd MMM yyyy"
    private const val API_DATE_FORMAT = "yyyy-MM-dd"

    @SuppressLint("SimpleDateFormat")
    fun convertDateWithoutYear(date: String): String {
        val date = SimpleDateFormat(API_DATE_FORMAT).parse(date)
        return SimpleDateFormat(UI_DATE_FORMAT, Locale.getDefault()).format(date)
    }


    fun convertDate(date: String): String {
        val date = SimpleDateFormat(API_DATE_FORMAT).parse(date)
        return SimpleDateFormat(UI_DATE_FORMAT_FULL, Locale.getDefault()).format(date)
    }


    @SuppressLint("SimpleDateFormat")
    fun getAge(dateString: String): String {
        val date = SimpleDateFormat(API_DATE_FORMAT).parse(dateString)
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()

        dob.set(date.year + 1900, date.month, date.day)

        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt.toString()
    }
}