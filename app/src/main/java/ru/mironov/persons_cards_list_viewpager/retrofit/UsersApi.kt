package ru.mironov.persons_cards_list_viewpager.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface UsersApi {

    @Headers("Prefer: code=200, dynamic=true")
    @GET("users")
    fun getUsers(): Call<JsonArrayUsers>

}
