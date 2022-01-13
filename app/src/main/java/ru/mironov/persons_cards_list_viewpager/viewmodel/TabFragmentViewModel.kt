package ru.mironov.persons_cards_list_viewpager.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.mironov.persons_cards_list_viewpager.Repository
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TabFragmentViewModel @Inject constructor(protected val repository: Repository) : ViewModel() {

    var mutableStatus = MutableLiveData<Status>()

    @RequiresApi(Build.VERSION_CODES.N)
    fun getUsersByDepartment(department: String?) {
        val list = repository.usersList

        viewModelScope.launch(Dispatchers.Default) {

            list?.stream()
            val status = Status.DATA(null)

            status.usersList =
                list?.filter { s -> s?.department == department } as ArrayList<JsonUser?>?
            mutableStatus.postValue(status)
        }
    }

    fun getUsersAll() {
        val list = repository.usersList

        val status = Status.DATA(null)

        status.usersList = list
        mutableStatus.postValue(status)
    }
}