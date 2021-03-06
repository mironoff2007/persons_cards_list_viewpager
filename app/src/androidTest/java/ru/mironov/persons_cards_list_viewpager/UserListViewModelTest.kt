package ru.mironov.persons_cards_list_viewpager

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.data.retrofit.UsersApi
import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import ru.mironov.persons_cards_list_viewpager.domain.SortBy
import ru.mironov.persons_cards_list_viewpager.domain.SortParams
import ru.mironov.persons_cards_list_viewpager.domain.Status
import ru.mironov.persons_cards_list_viewpager.presentation.viewmodel.UsersListFragmentViewModel
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class UserListViewModelTest {

    private lateinit var viewModelUserList: UsersListFragmentViewModel
    private val repository = Repository(mock(UsersApi::class.java))

    private var result: Status?=null
    private lateinit var observer: Observer<Status>

    private lateinit var context: Context

    companion object{
        const val TIME_STEP=100L
        const val ERROR_MESSAGE="LiveData value was never set."
        val timeUnit = TimeUnit.SECONDS
    }

    private var latch = CountDownLatch(1)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        result = null

        context = InstrumentationRegistry.getInstrumentation().targetContext
        val jsonString = context.resources.openRawResource(R.raw.users_response).bufferedReader()
            .use { it.readText() }

        repository.usersList = Gson().fromJson<ArrayList<JsonUser?>>(
            jsonString,
            object : TypeToken<ArrayList<JsonUser?>>() {}.type
        )

        viewModelUserList = UsersListFragmentViewModel(repository)
        viewModelUserList.allDepartmentName = context.getString(R.string.department_all)

        setupObserver()
        latch=CountDownLatch(1)
    }


    /**
     * ?????????? ???????? ?????? ?????????? ?????????? ?? ?????????????????? ??????????????
     * ?????????? ???????????????? ?????????????????????? ???? ????????????????,
     * ???? ?????????????? ???????? ???????????? ?? ??????????????
     */

    @Test
    fun checkListSize() {

        runBlocking {
            viewModelUserList.listenSearchParam(viewModelUserList.allDepartmentName)
            repository.mutableSearchParam.value = SortParams("", SortBy.ALPHABET_SORT)
        }
        //Wait
        if (!latch.await(TIME_STEP, timeUnit)) { throw TimeoutException(ERROR_MESSAGE) }

        assert((result as Status.DATA).usersList!!.size==80)
    }


    @Test
    fun testSearchByNameAndDepartment() {

        runBlocking {
            viewModelUserList.listenSearchParam(context.getString(R.string.department_android))
            repository.mutableSearchParam.value = SortParams("Hanna", SortBy.ALPHABET_SORT)
        }

        //Wait
        if (!latch.await(TIME_STEP, timeUnit)) { throw TimeoutException(ERROR_MESSAGE) }

        val lastName = (result as Status.DATA).usersList!![0]!!.lastName
        assert((result as Status.DATA).usersList!!.size == 1 && lastName == "Kling")
    }

    @Test
    fun testEmpty() {
        runBlocking {
            viewModelUserList.listenSearchParam(viewModelUserList.allDepartmentName)
            repository.mutableSearchParam.value = SortParams("not_match", SortBy.ALPHABET_SORT)
        }

        //Wait
        if (!latch.await(TIME_STEP, timeUnit)) { throw TimeoutException(ERROR_MESSAGE) }

        assert(result == Status.EMPTY)
    }

    @Test
    fun testSearchByDepartment() {
        runBlocking {
            viewModelUserList.listenSearchParam(context.getString(R.string.department_android))
            repository.mutableSearchParam.value = SortParams("", SortBy.ALPHABET_SORT)
        }

        //Wait
        if (!latch.await(TIME_STEP, timeUnit)) { throw TimeoutException(ERROR_MESSAGE) }

        assert((result as Status.DATA)?.usersList!!.size==10)
    }

    private fun setupObserver() {
        observer = Observer<Status> { status ->
            when (status) {
                is Status.DATA -> {
                    result = status
                    latch.countDown()
                }
                is Status.LOADING -> {}
                is Status.ERROR -> {
                    result = status
                    latch.countDown()
                }
                is Status.EMPTY -> {
                    result = status
                    latch.countDown()
                }
            }
        }
        viewModelUserList.mutableStatus.observeForever(observer)
    }

    @After
    fun after(){
        viewModelUserList.mutableStatus.removeObserver(observer)
    }
}
