package ru.mironov.persons_cards_list_viewpager.ui

import android.os.Bundle
import android.os.Debug
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import ru.mironov.persons_cards_list_viewpager.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var tabLayout: TabLayout? = null
    private var pager2: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Debug.waitForDebugger()

        tabLayout = findViewById(R.id.tabs);
        pager2 = findViewById(R.id.view_pager);

        setupObserver()

        viewModel.allUsersDepartment=applicationContext.getString(R.string.department_all)
        viewModel.getUsers()

    }

    private fun setUpViewPager(tabName: Array<String>) {
        val adapter = ViewPagerAdapter(this)
        adapter.tabName = tabName
        pager2!!.adapter = adapter
        TabLayoutMediator(
            tabLayout!!, pager2!!
        ) { tab, position -> tab.text =
            DepartmentNameUtil.getDepartmentName(
                applicationContext,
                tabName[position]
            )
        }.attach()
    }

    private fun setupObserver() {
        viewModel.mutableStatus.observe(this) { status ->
            when (status) {

                is Status.DATA -> {
                    setUpViewPager(status.departments!!)
                }
                is Status.LOADING -> {

                }
                is Status.ERROR -> {
                    Toast.makeText(this.applicationContext, status.code, Toast.LENGTH_LONG)?.show()
                }
            }
        }
    }
}

