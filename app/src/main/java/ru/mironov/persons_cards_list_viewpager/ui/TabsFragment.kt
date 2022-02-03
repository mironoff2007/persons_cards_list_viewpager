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
import ru.mironov.persons_cards_list_viewpager.data.SortParams
import ru.mironov.persons_cards_list_viewpager.databinding.*
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import ru.mironov.persons_cards_list_viewpager.viewmodel.FragmentTabsViewModel
import ru.mironov.persons_cards_list_viewpager.viewmodel.Status


@AndroidEntryPoint
class TabsFragment : Fragment() {

    private val viewModel: FragmentTabsViewModel by viewModels()

    private var tabLayout: TabLayout? = null
    private var pager2: ViewPager2? = null
    private var adapter: ViewPagerAdapter?=null

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
            sortBy = savedInstanceState.getSerializable(ARG_SORT) as SortBy
            searchBy = savedInstanceState.getString(ARG_SEARCH)!!
        }
        viewModel.allUsersDepartment = requireContext().getString(R.string.department_all)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabsBinding.inflate(inflater, container, false)
        _bindingDialog = BottomsheetlayoutBinding.inflate(inflater, container, false)

        tabLayout = binding.tabs
        pager2 = binding.viewPager

        setupListeners()
        setupObserver()
        setUpViewPager()

        viewModel.getUsersCheckCache()

        binding.search.setText(searchBy)
        setSort(sortBy)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupListeners() {
        binding.search.addTextChangedListener(textChangeListener)
        binding.cancelSearch.setOnClickListener { binding.search.text.clear() }
        binding.search.setOnTouchListener(View.OnTouchListener { v, event ->
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

    fun setSort(value: SortBy) {
        if (value == SortBy.ALPHABET_SORT) {
            bindingDialog.radioGroup.check(R.id.radio_alphabetSort)
            binding.search.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_search,
                0,
                R.drawable.ic_sort_off,
                0
            )
        } else {
            bindingDialog.radioGroup.check(R.id.radio_birthDaySort)
            binding.search.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_search, 0,
                R.drawable.ic_sort_on, 0
            )
        }
    }

    private fun setUpViewPager() {

        adapter = ViewPagerAdapter(this)
        adapter!!.tabNames = requireContext().resources.getStringArray(R.array.department_names_api)
        pager2!!.adapter = adapter
        TabLayoutMediator(
            tabLayout!!, pager2!!
        ) { tab, position ->
            tab.text =
                DepartmentNameUtil.getDepartmentName(
                    requireContext(),
                    adapter!!.tabNames[position]
                )
        }.attach()
    }

    fun update() {
        position = 0
        viewModel.setSearchParam(null)
        viewModel.getUsers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObserver() {
        viewModel.mutableStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.DATA -> {
                    ResultRenderer.renderResult(status, binding.root.partResult)
                    viewModel.setSearchParam(SortParams(searchBy, sortBy))
                    binding.viewPager.setCurrentItem(position, false)
                }
                is Status.LOADING -> {
                    ResultRenderer.renderResult(status, binding.root.partResult)
                    binding.root.partResult.resultTextBottom.setOnClickListener(null)
                }
                is Status.ERROR -> {
                    binding.root.partResult.resultTextBottom.setOnClickListener { viewModel.getUsers() }

                    if (status.code == 0) {
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.no_internet),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(requireContext(), status.message, Toast.LENGTH_LONG).show()
                    }
                    if (viewModel.isUsersEmpty()) {
                        ResultRenderer.renderResult(status, binding.root.partResult)
                    } else {
                        ResultRenderer.renderResult(Status.DATA(null), binding.root.partResult)
                    }
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
            searchBy = s.toString()
            if(binding.search.hasFocus()) {
                viewModel.setSearchParam(SortParams(searchBy, sortBy))
            }
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
            viewModel.setSearchParam(SortParams(searchBy, sortBy))
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onDestroyView() {
        super.onDestroyView()
        binding.cancelSearch.setOnClickListener (null)
        binding.search.setOnTouchListener(null)
        binding.search.removeTextChangedListener(textChangeListener)
        bindingDialog.radioGroup.setOnCheckedChangeListener(null)
        binding.root.partResult.resultTextBottom.setOnClickListener(null)
        tabLayout = null
        pager2 = null
        adapter =null
        _binding = null
        _bindingDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        const val ARG_POSITION_TAB = "ARG_POSITION_TAB"
        const val ARG_SEARCH = "ARG_SEARCH"
        const val ARG_SORT = "ARG_SORT"
        const val TAG_TABS_FRAGMENT = "TAG_TABS_FRAGMENT"
        const val DRAWABLE_RIGHT = 2
    }
}