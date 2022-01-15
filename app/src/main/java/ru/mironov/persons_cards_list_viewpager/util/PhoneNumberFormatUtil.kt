package ru.mironov.persons_cards_list_viewpager.util

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

object PhoneNumberFormatUtil {

    fun formatNumber(number:String):String{
        val pnu: PhoneNumberUtil = PhoneNumberUtil.getInstance()
        val pn: Phonenumber.PhoneNumber = pnu.parse(number, "RU")
        return pnu.format(pn, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
    }
}