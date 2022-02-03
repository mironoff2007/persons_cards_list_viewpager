package ru.mironov.persons_cards_list_viewpager


import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.part_result.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Assert.*
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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var job: Job

    private lateinit var viewModelUserList: UsersListFragmentViewModel
    private val repository = Repository(mock(UsersApi::class.java))

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
    fun useAppContext() {
        // Context of the app under test.

        repository.mutableSearchParam.value = SortParams("", SortBy.ALPHABET_SORT)

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.mironov.persons_cards_list_viewpager", appContext.packageName)
    }

    @After
    fun closeJob()
    {
        job.cancel()
    }


    private fun setupObserver() {
        job = GlobalScope.launch(Dispatchers.Main) {
            viewModelUserList.mutableStatus.observeForever() { status ->
                when (status) {

                    is Status.DATA -> {
                        Log.d("My_tag", status.usersList.toString())
                        job.cancel()
                    }
                    is Status.LOADING -> {
                        job.cancel()

                    }
                    is Status.ERROR -> {
                        job.cancel()
                    }
                    is Status.EMPTY -> {
                        job.cancel()
                    }
                }
            }
        }
    }
}
