package ru.mironov.persons_cards_list_viewpager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var tabLayout: TabLayout? = null
    private var pager2: ViewPager2? = null

    val tabName = arrayOf("1", "2", "3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Debug.waitForDebugger()

        tabLayout = findViewById(R.id.tabs);
        pager2 = findViewById(R.id.view_pager);
        setUpViewPager(tabName)

        viewModel.getUsers()

    }

    private fun setUpViewPager(tabName:Array<String>) {
        val adapter = ViewPagerAdapter(this)
        adapter.tabName=tabName
        pager2!!.adapter = adapter
        TabLayoutMediator(
            tabLayout!!, pager2!!
        ) { tab, position -> tab.text = tabName[position] }.attach()
    }
}
