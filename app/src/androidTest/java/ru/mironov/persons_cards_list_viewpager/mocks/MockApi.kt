package ru.mironov.persons_cards_list_viewpager.mocks


import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mironov.persons_cards_list_viewpager.domain.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.data.retrofit.UsersApi

class MockApiError : UsersApi {
    override fun getUsers(): Call<JsonArrayUsers> {
        return object : Call<JsonArrayUsers> {
            override fun clone(): Call<JsonArrayUsers> {
                return this
            }

            override fun execute(): Response<JsonArrayUsers> {
                val errorString = "error"

                return Response.error<JsonArrayUsers>(
                    400,
                    errorString.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())
                )
            }

            override fun isExecuted(): Boolean {
                return false
            }


            override fun isCanceled(): Boolean {
                return false
            }

            override fun cancel() {

            }

            override fun request(): Request {
                return Request.Builder().build()
            }

            override fun timeout(): Timeout {
                return Timeout()
            }

            override fun enqueue(callback: Callback<JsonArrayUsers>) {
                callback.onResponse(clone(),execute())
            }
        }
    }
}


