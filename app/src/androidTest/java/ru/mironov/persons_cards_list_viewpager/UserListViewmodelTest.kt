package ru.mironov.persons_cards_list_viewpager


import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.data.SortParams
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi
import ru.mironov.persons_cards_list_viewpager.viewmodel.Status
import ru.mironov.persons_cards_list_viewpager.viewmodel.UsersListFragmentViewModel
import java.lang.Thread.sleep
import java.util.concurrent.locks.ReentrantLock

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UserListViewmodelTest {

    private lateinit var job: Job

    private lateinit var viewModelUserList: UsersListFragmentViewModel
    private val repository = Repository(mock(UsersApi::class.java))

    private lateinit var resultData: Status.DATA
    private lateinit var resultStatus: Status

    var locked = true

    @Before
    fun setUp() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val jsonString = context.resources.openRawResource(R.raw.users_response).bufferedReader()
            .use { it.readText() }

        repository.usersList = Gson().fromJson<ArrayList<JsonUser?>>(
            jsonString,
            object : TypeToken<ArrayList<JsonUser?>>() {}.type
        )

        viewModelUserList = UsersListFragmentViewModel(repository)
        setupObserver()
        viewModelUserList.allDepartmentName = context.getString(R.string.department_all)
        viewModelUserList.listenSearchParam(viewModelUserList.allDepartmentName)
    }

    @Test
    fun checkListSize() {

        repository.mutableSearchParam.value = SortParams("", SortBy.ALPHABET_SORT)

        while (locked) {
            sleep(100)
        }

        assert(resultData.usersList!!.size==80)
    }

    @Test
    fun testSearchByDepartment() {
        viewModelUserList.allDepartmentName = context.getString(R.string.department_android)

        locked=true
        repository.mutableSearchParam.value = SortParams("", SortBy.ALPHABET_SORT)

        while (locked) {
            sleep(100)
        }

        assert(resultData.usersList!!.size==10)
    }

    @Test
    fun testSearchByName() {
        viewModelUserList.allDepartmentName = context.getString(R.string.department_android)

        locked=true
        repository.mutableSearchParam.value = SortParams("Hanna", SortBy.ALPHABET_SORT)

        while (locked) {
            sleep(100)
        }
        val lastName= resultData!!.usersList!![0].lastName

        assert(resultData.usersList!!.size==1&& lastName=="Kling")
    }

    @Test
    fun testEmpty() {
        viewModelUserList.allDepartmentName = context.getString(R.string.department_android)

        locked=true
        repository.mutableSearchParam.value = SortParams("not_match", SortBy.ALPHABET_SORT)

        while (locked) {
            sleep(100)
        }

        assert(resultStatus.equals(Status.EMPTY))
    }

    @After
    fun closeJob() {
        job.cancel()
    }


    private fun setupObserver() {
        job = GlobalScope.launch(Dispatchers.Main) {
            viewModelUserList.mutableStatus.observeForever() { status ->
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
