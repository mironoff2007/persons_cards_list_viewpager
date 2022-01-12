package ru.mironov.persons_cards_list_viewpager

import retrofit2.Call
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi
import javax.inject.Inject

open class Repository @Inject constructor(protected val retrofit: UsersApi) {


    fun getUsers(): Call<JsonArrayUsers> {
        return retrofit.getUsers()
    }
}
