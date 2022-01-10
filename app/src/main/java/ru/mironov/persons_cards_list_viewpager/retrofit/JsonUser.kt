package ru.mironov.persons_cards_list_viewpager.retrofit

import com.google.gson.annotations.SerializedName

class JsonUser {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("firstName")
    var firstName: String? = null

}