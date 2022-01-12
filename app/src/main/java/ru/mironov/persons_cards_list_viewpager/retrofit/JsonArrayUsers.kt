package ru.mironov.persons_cards_list_viewpager.retrofit

import com.google.gson.annotations.SerializedName

class JsonArrayUsers {

    @SerializedName("items")
    var users: ArrayList<JsonUser?>? = null
}