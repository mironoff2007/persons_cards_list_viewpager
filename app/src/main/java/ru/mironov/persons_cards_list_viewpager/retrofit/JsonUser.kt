package ru.mironov.persons_cards_list_viewpager.retrofit

import com.google.gson.annotations.SerializedName

class JsonUser {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("avatarUrl")
    var avatarUrl: String? = null

    @SerializedName("userTag")
    var userTag: String? = null

    @SerializedName("department")
    var department: String? = null

    @SerializedName("position")
    var position: String? = null

    @SerializedName("birthday")
    var birthday: String? = null

    @SerializedName("phone")
    var phone: String? = null

}