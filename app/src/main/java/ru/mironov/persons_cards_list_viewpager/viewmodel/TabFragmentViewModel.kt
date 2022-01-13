package ru.mironov.persons_cards_list_viewpager.viewmodel

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mironov.persons_cards_list_viewpager.Repository
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TabFragmentViewModel @Inject constructor(protected val repository: Repository) : ViewModel() {

    lateinit var allDepartmentName: String

    var mutableStatus = MutableLiveData<Status>()

    var observer: Observer? = null

    var usersList:ArrayList<JsonUser?>?=null

    init {
        listenSearchParam()
    }

    fun getUsersByDepartment(department: String?) {
        val list = repository.usersList

        viewModelScope.launch(Dispatchers.Default) {

            val status = Status.DATA(null)

            status.usersList =
                list?.filter { user -> user?.department == department || department == allDepartmentName } as ArrayList<JsonUser?>?

            usersList=status.usersList
            mutableStatus.postValue(status)
        }
    }

    private fun listenSearchParam() {
        repository.mutableSearchParam.observeForever() { param ->


            viewModelScope.launch(Dispatchers.Default) {

                val status = Status.DATA(null)

                status.usersList =
                    usersList?.filter { user ->
                        user?.lastName!!.lowercase().contains(param) ||
                                user?.firstName!!.lowercase().contains(param) ||
                                user?.userTag!!.lowercase().contains(param) ||
                                param.isEmpty()

                    } as ArrayList<JsonUser?>?
                mutableStatus.postValue(status)
            }
        }
    }


}