package ru.mironov.persons_cards_list_viewpager

import java.util.*

sealed class Status {
    object LOADING : Status()
    data class ERROR(var message:String,var code:Int) : Status()
    class DATA(val someData:List<Objects>?): Status()
    object RESPONSE : Status()
}
