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
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.data.SortParams
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.util.AvatarUrlFaker
import java.util.*
import javax.inject.Inject

@HiltViewModel

open class FragmentTabsViewModel @Inject constructor (protected val repository: Repository): ViewModel(){

    var mutableStatus = MutableLiveData<Status>()

    lateinit var allUsersDepartment:String

    var tabNames= emptyArray<String>()

    fun getUsersCheckCache(){
        if(repository.usersList?.isEmpty()==true||repository.usersList==null){
            getUsers()
        }
        else{
            mutableStatus.postValue(Status.DATA(repository.departments!!.toTypedArray()))
        }
    }

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
                        val departments= TreeSet<String>()
                        departments.add(allUsersDepartment)
                        usersList?.forEach {
                            departments.add(it?.department.toString())
                        }

                        //KODE api provides images url from expired domain
                        usersList?.forEach { it->it?.avatarUrl=AvatarUrlFaker.getUrl() }

                        repository.usersList=usersList
                        repository.departments=departments
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

    fun setSearchParam(param: String,sortBy: SortBy) {
        repository.mutableSearchParam.value= SortParams(param,sortBy)
    }

    fun isUsersEmpty(): Boolean {
        return repository.usersList?.isEmpty()==true||repository.usersList==null
    }

}