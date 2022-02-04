package ru.mironov.persons_cards_list_viewpager.data

import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import ru.mironov.persons_cards_list_viewpager.domain.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import ru.mironov.persons_cards_list_viewpager.data.retrofit.UsersApi
import ru.mironov.persons_cards_list_viewpager.domain.SortParams
import javax.inject.Inject
import kotlin.collections.ArrayList

//да, можно сделать интерфейс и не надо делать open для затычки в тестах
open class Repository @Inject constructor(protected val retrofit: UsersApi) {

    var usersList:ArrayList<JsonUser?>?=null

    var mutableSearchParam = MutableStateFlow<SortParams?>(null)

    open fun getUsers(): Call<JsonArrayUsers> {
        return retrofit.getUsers()
    }
}
