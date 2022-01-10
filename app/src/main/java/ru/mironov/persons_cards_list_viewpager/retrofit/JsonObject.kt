package ru.mironov.persons_cards_list_viewpager.retrofit

import com.google.gson.annotations.SerializedName

class JsonObject {

    @SerializedName("items")
    var users: ArrayList<JsonUser?>? = null
}