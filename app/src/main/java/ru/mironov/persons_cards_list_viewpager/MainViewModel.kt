package ru.mironov.persons_cards_list_viewpager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import java.util.HashSet
import javax.inject.Inject

@HiltViewModel

class MainViewModel @Inject constructor (protected val repository:Repository): ViewModel(){

    var mutableStatus = MutableLiveData<Status>()

    var departments=HashSet<String>()

    fun getUsers(){
        repository.getUsers().enqueue(object : Callback<JsonArrayUsers?> {
            override fun onResponse(
                call: Call<JsonArrayUsers?>,
                response: Response<JsonArrayUsers?>
            ) {
                if (response.body() != null) {
                    viewModelScope.launch(Dispatchers.Default) {

                        val list = response.body()!!.users

                        list?.forEach(){
                            departments?.add(it?.department.toString())
                        }
                        mutableStatus.postValue(Status.RESPONSE)
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

}