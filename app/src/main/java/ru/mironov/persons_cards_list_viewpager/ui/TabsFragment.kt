package ru.mironov.persons_cards_list_viewpager.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.part_result.view.*
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.viewmodel.Status
import ru.mironov.persons_cards_list_viewpager.databinding.*
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import ru.mironov.persons_cards_list_viewpager.viewmodel.FragmentTabsViewModel


@AndroidEntryPoint
class TabsFragment : Fragment() {

    private val viewModel: FragmentTabsViewModel by viewModels()

    private var tabLayout: TabLayout? = null
    private var pager2: ViewPager2? = null

    private var _binding: FragmentTabsBinding? = null
    private var _bindingDialog: BottomsheetlayoutBinding? = null

    private val binding get() = _binding!!
    private val bindingDialog get() = _bindingDialog!!

    var searchBy: String = ""
    var sortBy = SortBy.ALPHABET_SORT


    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(ARG_POSITION_TAB)
            sortBy= savedInstanceState.getSerializable(ARG_SORT) as SortBy
            searchBy=savedInstanceState.getString(ARG_SEARCH)!!
        }
        setupObserver()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabsBinding.inflate(inflater, container, false)
        _bindingDialog = BottomsheetlayoutBinding.inflate(inflater, container, false)

        tabLayout = binding.tabs
        pager2 = binding.viewPager

        setupListeners()

        viewModel.allUsersDepartment = requireContext().getString(R.string.department_all)
        viewModel.getUsers()

        binding.search.setText(searchBy)
        setSort(sortBy)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        binding.search.addTextChangedListener(textChangeListener)
        binding.cancelSearch.setOnClickListener { binding.search.text.clear() }
        binding.search.setOnTouchListener(View.OnTouchListener { v, event ->
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

    fun setSort(value: SortBy){
        if (value == SortBy.ALPHABET_SORT) {
            bindingDialog.radioGroup.check(R.id.radio_alphabetSort)
            binding.search.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_search,
                0,
                R.drawable.ic_baseline_sort_off,
                0
            )
        } else {
            bindingDialog.radioGroup.check(R.id.radio_birthDaySort)
            binding.search.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_baseline_search, 0,
                R.drawable.ic_baseline_sort_on, 0
            )
        }
    }

    private fun setUpViewPager(tabName: Array<String>) {
        val adapter = ViewPagerAdapter(activity!!)
        adapter.tabNames = tabName
        pager2!!.adapter = adapter
        TabLayoutMediator(
            tabLayout!!, pager2!!
        ) { tab, position ->
            tab.text =
                DepartmentNameUtil.getDepartmentName(
                    requireContext(),
                    tabName[position]
                )
        }.attach()
    }

    private fun setupObserver() {
        viewModel.mutableStatus.observe(this) { status ->
            when (status) {
                is Status.DATA -> {
                    ResultRenderer.renderResult(status,binding.root.part_result)
                    viewModel.setSearchParam(searchBy, sortBy)
                    setUpViewPager(status.departments!!)
                    binding.viewPager.setCurrentItem(position, false)
                }
                is Status.LOADING -> {
                    ResultRenderer.renderResult(status,binding.root.part_result)
                    binding.root.part_result.resultTextBottom.setOnClickListener(null)

                }
                is Status.ERROR -> {
                    binding.root.part_result.resultTextBottom.setOnClickListener { viewModel.getUsers() }
                    ResultRenderer.renderResult(status,binding.root.part_result)
                    Toast.makeText(requireContext(), status.message, Toast.LENGTH_LONG).show()
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
            searchBy = s.toString().lowercase()
            viewModel.setSearchParam(searchBy, sortBy)
        }

        override fun afterTextChanged(editable: Editable) {}
    }

    private fun showDialog() {
        val dialog = Dialog(requireContext())
        _bindingDialog = BottomsheetlayoutBinding.inflate(layoutInflater)

        bindingDialog.radioAlphabetSort.isChecked = SortBy.ALPHABET_SORT == sortBy
        bindingDialog.radioBirthDaySort.isChecked = SortBy.BIRTHDAY_SORT == sortBy

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(bindingDialog.root)

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        bindingDialog.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_alphabetSort -> {
                    sortBy = SortBy.ALPHABET_SORT
                    setSort(sortBy)
                }
                R.id.radio_birthDaySort -> {
                    sortBy = SortBy.BIRTHDAY_SORT
                    setSort(sortBy)
                }
            }
            viewModel.setSearchParam(searchBy, sortBy)
            dialog.dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_POSITION_TAB, position)
        outState.putString(ARG_SEARCH, searchBy)
        outState.putSerializable(ARG_SORT, sortBy)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        position = binding.viewPager.currentItem
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.search.removeTextChangedListener(textChangeListener)
        bindingDialog.radioGroup.setOnCheckedChangeListener(null)
        binding.root.part_result.resultTextBottom.setOnClickListener(null)
        _binding = null
        _bindingDialog = null
    }

    companion object {
        const val ARG_POSITION_TAB = "ARG_POSITION_TAB"
        const val ARG_SEARCH = "ARG_SEARCH"
        const val ARG_SORT = "ARG_SORT"
    }
}