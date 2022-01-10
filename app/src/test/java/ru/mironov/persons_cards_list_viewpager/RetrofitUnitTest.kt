package ru.mironov.persons_cards_list_viewpager

import com.google.gson.GsonBuilder
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonObject
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi

class RetrofitUnitTest {
    @Test
    fun retrofitTest() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://stoplight.io/mocks/kode-education/trainee-test/25143926/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls()
                        .create()
                )
            ).build()
        val call: Call<JsonObject> = retrofit.create(UsersApi::class.java).getUsers()

        val response: Response<JsonObject> = call!!.execute()

        var body: JsonObject? = null
        if (response.body() != null) {
            body = response.body()
        }

        assertEquals(true, body != null)
    }
}
