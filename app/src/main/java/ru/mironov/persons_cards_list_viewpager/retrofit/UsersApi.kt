package ru.mironov.persons_cards_list_viewpager.retrofit


import retrofit2.Call
import retrofit2.http.GET

interface UsersApi {

    @GET("users")
    fun getUsers(
    ): Call<JsonObject>

}
