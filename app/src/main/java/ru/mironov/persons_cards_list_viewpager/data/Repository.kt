package ru.mironov.persons_cards_list_viewpager.data

import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi
import javax.inject.Inject

open class Repository @Inject constructor(protected val retrofit: UsersApi) {

    var usersList:ArrayList<JsonUser?>?=null

    var mutableSearchParam = MutableStateFlow<SortParams?>(null)

    fun getUsers(): Call<JsonArrayUsers> {
        return retrofit.getUsers()
    }
}
