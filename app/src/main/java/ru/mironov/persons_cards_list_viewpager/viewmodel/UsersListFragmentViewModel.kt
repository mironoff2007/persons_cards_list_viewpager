package ru.mironov.persons_cards_list_viewpager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.data.SortParams
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UsersListFragmentViewModel @Inject constructor(protected val repository: Repository) :
    ViewModel() {

    lateinit var allDepartmentName: String

    var mutableStatus = MutableLiveData<Status>()

    fun getParams(): SortParams? {
        return repository.mutableSearchParam.value
    }

    private fun getUsersWithSort(department: String, params: SortParams) {

        mutableStatus.postValue(Status.LOADING)

        val list = repository.usersList

        viewModelScope.launch(Dispatchers.Default) {

            val status = Status.DATA(null)

            //Filter by departments and search
            status.usersList =
                list?.filter { user ->
                    (user?.department == department || department == allDepartmentName) &&
                            ((user?.firstName!! + " " + user.lastName!!).lowercase()
                                .contains(params.searchBy) ||
                                    user.userTag!!.lowercase().contains(params.searchBy) ||
                                    params.searchBy.isEmpty())
                } as ArrayList<JsonUser?>?

            //Sort
            if (params.sortBy == SortBy.ALPHABET_SORT) {
                status.usersList?.sortBy { it?.firstName }
            } else {
                status.usersList?.sortBy { it?.birthday }
            }
            if (status.usersList!!.isEmpty()) {
                mutableStatus.postValue(Status.EMPTY)
            } else {
                mutableStatus.postValue(status)
            }
        }
    }

    fun listenSearchParam(department: String) {
        repository.mutableSearchParam.onEach { params ->
            if (params != null) {
                getUsersWithSort(department, params)
            }
        }.launchIn(viewModelScope)
    }

}