package ru.mironov.persons_cards_list_viewpager.ui

import android.os.Bundle
import android.os.Debug
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.databinding.ActivityMainBinding
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentTabBinding
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import ru.mironov.persons_cards_list_viewpager.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var tabLayout: TabLayout? = null
    private var pager2: ViewPager2? = null

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Debug.waitForDebugger()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = findViewById(R.id.tabs);
        pager2 = findViewById(R.id.view_pager);

        setupObserver()
        binding.search.addTextChangedListener(textChangeListener)
        binding.cancelSearch.setOnClickListener { binding.search.text.clear() }

        viewModel.allUsersDepartment = applicationContext.getString(R.string.department_all)
        viewModel.getUsers()

    }

    private fun setUpViewPager(tabName: Array<String>) {
        val adapter = ViewPagerAdapter(this)
        adapter.tabName = tabName
        pager2!!.adapter = adapter
        TabLayoutMediator(
            tabLayout!!, pager2!!
        ) { tab, position ->
            tab.text =
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

    private val textChangeListener: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isEmpty()) {
                binding.cancelSearch.visibility = View.GONE
            } else {
                binding.cancelSearch.visibility = View.VISIBLE
            }
            viewModel.setSearchParam(s.toString().lowercase())
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.search.removeTextChangedListener(textChangeListener)
        _binding = null
    }
}


