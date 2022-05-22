package ru.mironov.persons_cards_list_viewpager.presentation.viewmodel

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
import ru.mironov.persons_cards_list_viewpager.domain.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.domain.SortParams
import ru.mironov.persons_cards_list_viewpager.domain.Status
import ru.mironov.persons_cards_list_viewpager.domain.util.AvatarUrlFaker
import javax.inject.Inject

@HiltViewModel

open class FragmentTabsViewModel @Inject constructor (protected val repository: Repository): ViewModel(){

    var mutableStatus = MutableLiveData<Status>()

    lateinit var allUsersDepartment:String

    fun getUsersCheckCache(){
        if(repository.usersList?.isEmpty() == true || repository.usersList == null){
            getUsers()
        }
        else{
            mutableStatus.postValue(Status.DATA(null))
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

                        //KODE api provides images url from expired domain
                        usersList?.forEach { it->it?.avatarUrl=AvatarUrlFaker.getUrl() }

                        repository.usersList=usersList
                        mutableStatus.postValue(Status.DATA(null))
                    }
                } else {
                    if (response.errorBody() != null) {
                        mutableStatus.postValue(
                            Status.ERROR(
                                response.errorBody().toString(),
                                response.raw().code
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

    fun setSearchParam(param: SortParams?) {

        repository.mutableSearchParam.value = param
    }

    fun isUsersEmpty(): Boolean {
        return repository.usersList?.isEmpty() == true || repository.usersList == null
    }

}