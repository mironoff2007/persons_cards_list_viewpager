package ru.mironov.persons_cards_list_viewpager.data

import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

open class Repository @Inject constructor(protected val retrofit: UsersApi) {

    var usersList:ArrayList<JsonUser?>?=null

    var departments: TreeSet<String>?=null

    var mutableSearchParam = MutableStateFlow<SortParams?>(null)

    fun getUsers(): Call<JsonArrayUsers> {
        return retrofit.getUsers()
    }
}
