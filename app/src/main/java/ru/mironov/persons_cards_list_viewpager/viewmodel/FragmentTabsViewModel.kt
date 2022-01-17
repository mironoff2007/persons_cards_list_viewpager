package ru.mironov.persons_cards_list_viewpager.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mironov.persons_cards_list_viewpager.Repository
import ru.mironov.persons_cards_list_viewpager.SortBy
import ru.mironov.persons_cards_list_viewpager.SortParams
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import java.util.HashSet
import javax.inject.Inject

@HiltViewModel

class FragmentTabsViewModel @Inject constructor (protected val repository: Repository): ViewModel(){

    var mutableStatus = MutableLiveData<Status>()

    lateinit var allUsersDepartment:String

    fun getUsers(){
        mutableStatus.postValue(Status.LOADING)
        repository.getUsers().enqueue(object : Callback<JsonArrayUsers?> {
            override fun onResponse(
                call: Call<JsonArrayUsers?>,
                response: Response<JsonArrayUsers?>
            ) {
                if (response.body() != null) {
                    viewModelScope.launch(Dispatchers.Default) {

                        val usersList = response.body()!!.users

                        //Departments names for tabs
                        val departments=HashSet<String>()
                        departments.add(allUsersDepartment)
                        usersList?.forEach {
                            departments.add(it?.department.toString())
                        }

                        repository.usersList=usersList
                        mutableStatus.postValue(Status.DATA(departments.toTypedArray()))
                    }
                } else {
                    if (response.errorBody() != null) {
                        mutableStatus.postValue(
                            Status.ERROR(
                                response.errorBody().toString(),
                                response.raw().code()
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<JsonArrayUsers?>, t: Throwable) {
                mutableStatus.postValue(Status.ERROR(t.message.toString(), 0))
            }
        })
    }

    fun setSearchParam(param: String,sortBy:SortBy) {
        repository.mutableSearchParam.value= SortParams(param,sortBy)
    }

}