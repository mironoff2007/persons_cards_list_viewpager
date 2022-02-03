package ru.mironov.persons_cards_list_viewpager


import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi

class MockApiError : UsersApi {
    override fun getUsers(): Call<JsonArrayUsers> {
        return object : Call<JsonArrayUsers> {
            override fun clone(): Call<JsonArrayUsers> {
                return this
            }

            override fun execute(): Response<JsonArrayUsers> {
                val errorString = "error"
                //ResponseBody.create(MediaType.parse("application/json")
                return Response.error<JsonArrayUsers>(
                    400, errorString.toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull()))
            }

            override fun enqueue(callback: Callback<JsonArrayUsers>) {
                TODO("Not yet implemented")
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
        }
    }
}


