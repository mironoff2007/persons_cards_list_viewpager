package ru.mironov.persons_cards_list_viewpager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.GsonBuilder
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class RetrofitInstrumentalUnitTest {

    @get:Rule
    var hiltRule=HiltAndroidRule(this)

    @Inject
    lateinit var retrofit:UsersApi

    @Test
    fun retrofitTest() {

        val call: Call<JsonArrayUsers> = retrofit.getUsers()

        val response: Response<JsonArrayUsers> = call!!.execute()

        var body: JsonArrayUsers? = null
        if (response.body() != null) {
            body = response.body()
        }

        assert(body!!.users!!.isNotEmpty())
    }
}
