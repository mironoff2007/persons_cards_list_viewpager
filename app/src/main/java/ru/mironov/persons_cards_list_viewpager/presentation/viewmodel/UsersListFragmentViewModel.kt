package ru.mironov.persons_cards_list_viewpager.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.domain.SortBy
import ru.mironov.persons_cards_list_viewpager.domain.SortParams
import ru.mironov.persons_cards_list_viewpager.domain.Status
import ru.mironov.persons_cards_list_viewpager.domain.util.SearchUtil
import javax.inject.Inject

@HiltViewModel
open class UsersListFragmentViewModel @Inject constructor(protected val repository: Repository) :
    ViewModel() {

    var position: Int = 0

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

            //Filter by departments and search by params
            status.usersList = SearchUtil.search(list,department,allDepartmentName,params)

            //Sort
            if (params.sortBy == SortBy.ALPHABET_SORT) {
                status.usersList?.sortBy { it?.firstName }
            } else {
                status.usersList?.sortBy { it?.birthday }
            }
            if (status.usersList?.isEmpty() == true) {
                mutableStatus.postValue(Status.EMPTY)
            } else if(status.usersList!=null) {
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