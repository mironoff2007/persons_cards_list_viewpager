package ru.mironov.persons_cards_list_viewpager.domain

import com.google.gson.annotations.SerializedName

class JsonArrayUsers {

    @SerializedName("items")
    var users: ArrayList<JsonUser?>? = null
}