package ru.mironov.persons_cards_list_viewpager

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.mironov.persons_cards_list_viewpager.mocks.MockRepository
import ru.mironov.persons_cards_list_viewpager.presentation.viewmodel.FragmentTabsViewModel
import ru.mironov.persons_cards_list_viewpager.domain.Status

@RunWith(AndroidJUnit4::class)
class TabsViewModelTest {

    private lateinit var viewModelUserTabs: FragmentTabsViewModel

    private val repository= MockRepository()

    private lateinit var resultData: Status.DATA
    private lateinit var resultStatus: Status
    private lateinit var observer:Observer<Status>

    lateinit var context: Context

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        viewModelUserTabs = FragmentTabsViewModel(repository)
        setupObserver()
    }

    @Test
    fun checkError() {
        runBlocking { viewModelUserTabs.getUsers() }

        assert(resultStatus is Status.ERROR)
    }

    private fun setupObserver() {
        observer = Observer<Status> { status ->
            when (status) {
                is Status.DATA -> { resultData = status }
                is Status.LOADING -> {}
                is Status.ERROR -> { resultStatus = status }
                is Status.EMPTY -> { resultStatus = status }
            }
        }
        viewModelUserTabs.mutableStatus.observeForever(observer)
    }

    @After
    fun after(){
        viewModelUserTabs.mutableStatus.removeObserver(observer)
    }
}
