package ru.mironov.persons_cards_list_viewpager

import androidx.test.ext.junit.runners.AndroidJUnit4

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Response
import ru.mironov.persons_cards_list_viewpager.domain.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.data.retrofit.UsersApi
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RetrofitInstrumentalUnitTest {


    @get:Rule
    var hiltRule=HiltAndroidRule(this)

    @Before
    fun init(){
        hiltRule.inject()
    }

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
