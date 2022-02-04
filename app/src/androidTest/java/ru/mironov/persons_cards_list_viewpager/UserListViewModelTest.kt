package ru.mironov.persons_cards_list_viewpager

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.domain.SortBy
import ru.mironov.persons_cards_list_viewpager.domain.SortParams
import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import ru.mironov.persons_cards_list_viewpager.data.retrofit.UsersApi
import ru.mironov.persons_cards_list_viewpager.domain.Status
import ru.mironov.persons_cards_list_viewpager.presentation.viewmodel.UsersListFragmentViewModel
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class UserListViewModelTest {

    private lateinit var job: Job

    private lateinit var viewModelUserList: UsersListFragmentViewModel
    private val repository = Repository(mock(UsersApi::class.java))

    private lateinit var resultData: Status.DATA
    private lateinit var resultStatus: Status

    private var locked = true

    private lateinit var context: Context

    @Before
    fun setUp() {

        context = InstrumentationRegistry.getInstrumentation().targetContext
        val jsonString = context.resources.openRawResource(R.raw.users_response).bufferedReader()
            .use { it.readText() }

        repository.usersList = Gson().fromJson<ArrayList<JsonUser?>>(
            jsonString,
            object : TypeToken<ArrayList<JsonUser?>>() {}.type
        )

        viewModelUserList = UsersListFragmentViewModel(repository)
        setupObserver()
        viewModelUserList.allDepartmentName = context.getString(R.string.department_all)
    }

    /**
     * После того как вынес поиск в отдельную функцию
     * стало возможно тестировать ее отдельно,
     * но оставил тест вместе с моделью
     */

    @Test
    fun checkListSize() {

        viewModelUserList.listenSearchParam(viewModelUserList.allDepartmentName)

        repository.mutableSearchParam.value = SortParams("", SortBy.ALPHABET_SORT)

        //да, костыль, но работает
        while (locked) {
            sleep(100)
        }

        assert(resultData.usersList!!.size==80)
    }


    @Test
    fun testSearchByNameAndDepartment() {
        viewModelUserList.listenSearchParam(context.getString(R.string.department_android))

        locked=true
        repository.mutableSearchParam.value = SortParams("Hanna", SortBy.ALPHABET_SORT)

        while (locked) {
            sleep(100)
        }
        val lastName= resultData.usersList!![0]!!.lastName

        assert(resultData.usersList!!.size==1&& lastName=="Kling")
    }

    @Test
    fun testEmpty() {
        viewModelUserList.listenSearchParam(viewModelUserList.allDepartmentName)

        locked=true
        repository.mutableSearchParam.value = SortParams("not_match", SortBy.ALPHABET_SORT)

        while (locked) {
            sleep(100)
        }

        assert(resultStatus == Status.EMPTY)
    }

    @Test
    fun testSearchByDepartment() {

        viewModelUserList.listenSearchParam(context.getString(R.string.department_android))

        locked=true
        repository.mutableSearchParam.value = SortParams("", SortBy.ALPHABET_SORT)

        while (locked) {
            sleep(100)
        }

        assert(resultData.usersList!!.size==10)
    }

    @After
    fun closeJob() {
        job.cancel()
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupObserver() {
        job = TestCoroutineScope().launch(Dispatchers.Main) {
            viewModelUserList.mutableStatus.observeForever() { status ->
                when (status) {

                    is Status.DATA -> {
                        resultData = status
                        locked=false
                        job.cancel()
                    }
                    is Status.LOADING -> {

                    }
                    is Status.ERROR -> {
                        resultStatus=status
                        locked=false
                        job.cancel()
                    }
                    is Status.EMPTY -> {
                        resultStatus=status
                        locked=false
                        job.cancel()
                    }
                }
            }
        }
    }
}
