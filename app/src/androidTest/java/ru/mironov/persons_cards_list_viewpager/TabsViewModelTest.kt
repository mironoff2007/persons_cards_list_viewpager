package ru.mironov.persons_cards_list_viewpager


import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.mironov.persons_cards_list_viewpager.mocks.MockRepository
import ru.mironov.persons_cards_list_viewpager.presentation.viewmodel.FragmentTabsViewModel
import ru.mironov.persons_cards_list_viewpager.domain.Status
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
class TabsViewModelTest {

    private lateinit var job: Job

    private lateinit var viewModelUserTabs: FragmentTabsViewModel

    val repository= MockRepository()

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


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupObserver() {
        job = TestCoroutineScope().launch(Dispatchers.Main) {
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
