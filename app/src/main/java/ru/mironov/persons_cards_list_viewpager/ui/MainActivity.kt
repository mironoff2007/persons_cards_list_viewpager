package ru.mironov.persons_cards_list_viewpager.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.SortBy
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.databinding.ActivityMainBinding
import ru.mironov.persons_cards_list_viewpager.databinding.BottomsheetlayoutBinding
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import ru.mironov.persons_cards_list_viewpager.viewmodel.MainViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private var tabLayout: TabLayout? = null
    private var pager2: ViewPager2? = null

    private var _binding: ActivityMainBinding? = null
    private var _bindingDialog: BottomsheetlayoutBinding? = null

    private val binding get() = _binding!!
    private val bindingDialog get() = _bindingDialog!!

    var searchBy:String=""
    var sortBy=SortBy.ALPHABET_SORT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Debug.waitForDebugger()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = findViewById(R.id.tabs);
        pager2 = findViewById(R.id.view_pager);

        setupObserver()
        setupListeners()

        viewModel.allUsersDepartment = applicationContext.getString(R.string.department_all)
        viewModel.getUsers()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        binding.search.addTextChangedListener(textChangeListener)
        binding.cancelSearch.setOnClickListener { binding.search.text.clear() }
        binding.search.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.search.right - binding.search.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                ) {
                    showDialog()
                    return@OnTouchListener true
                }
            }
            false
        })
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
                    viewModel.setSearchParam(searchBy,sortBy)
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
            searchBy=s.toString().lowercase()
            viewModel.setSearchParam(searchBy,sortBy)
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        _bindingDialog = BottomsheetlayoutBinding.inflate(layoutInflater)

        bindingDialog.radioAlphabetSort.isChecked=SortBy.ALPHABET_SORT==sortBy
        bindingDialog.radioBirthDaySort.isChecked=SortBy.BIRTHDAY_SORT==sortBy

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(bindingDialog.root)//R.layout.bottomsheetlayout)

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        bindingDialog.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_alphabetSort -> {
                    sortBy=SortBy.ALPHABET_SORT
                    binding.search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_search, 0, R.drawable.ic_baseline_sort_off, 0);

                }
                R.id.radio_birthDaySort -> {
                    sortBy=SortBy.BIRTHDAY_SORT
                    binding.search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_search, 0, R.drawable.ic_baseline_sort_on, 0);
                }
            }
            viewModel.setSearchParam(searchBy,sortBy)
            dialog.dismiss()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding.search.removeTextChangedListener(textChangeListener)
        _binding = null
        _bindingDialog=null
    }
}


