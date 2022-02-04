package ru.mironov.persons_cards_list_viewpager.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import ru.mironov.persons_cards_list_viewpager.domain.JsonArrayUsers

interface UsersApi {

    @Headers("Prefer: code=200, dynamic=true")
    @GET("users")
    fun getUsers(): Call<JsonArrayUsers>

}
