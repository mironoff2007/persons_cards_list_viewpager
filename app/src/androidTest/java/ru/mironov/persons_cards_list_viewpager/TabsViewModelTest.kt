package ru.mironov.persons_cards_list_viewpager


import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import okhttp3.Request
import okio.Timeout
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.data.SortParams
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonArrayUsers
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi
import ru.mironov.persons_cards_list_viewpager.viewmodel.FragmentTabsViewModel
import ru.mironov.persons_cards_list_viewpager.viewmodel.Status
import ru.mironov.persons_cards_list_viewpager.viewmodel.UsersListFragmentViewModel
import java.lang.Thread.sleep
import java.util.concurrent.locks.ReentrantLock


@RunWith(AndroidJUnit4::class)
class TabsViewModelTest {

    private lateinit var job: Job

    private lateinit var viewModelUserTabs: FragmentTabsViewModel

    val repository=MockRepository()

    private lateinit var resultData: Status.DATA
    private lateinit var resultStatus: Status

    var locked = true

    lateinit var context: Context

    @Before
    fun setUp() {

        context = InstrumentationRegistry.getInstrumentation().targetContext

        viewModelUserTabs = FragmentTabsViewModel(repository)
        setupObserver()

    }

    @Test
    fun checkError() {

        viewModelUserTabs.getUsers()

        while (locked) {
            sleep(100)
        }

        assert(resultStatus is Status.ERROR)
    }

    @After
    fun closeJob() {
        job.cancel()
    }


    private fun setupObserver() {
        job = TestCoroutineScope().launch(Dispatchers.Default) {
            viewModelUserTabs.mutableStatus.observeForever() { status ->
                when (status) {

                    is Status.DATA -> {
                        resultData = status
                        locked=false
                    }
                    is Status.LOADING -> {

                    }
                    is Status.ERROR -> {
                        resultStatus=status
                        locked=false
                    }
                    is Status.EMPTY -> {
                        resultStatus=status
                        locked=false
                    }
                }
            }
        }
    }
}
