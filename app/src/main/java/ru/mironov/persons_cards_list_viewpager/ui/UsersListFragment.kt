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
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.SortBy
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.databinding.ActivityMainBinding
import ru.mironov.persons_cards_list_viewpager.databinding.BottomsheetlayoutBinding
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentTabBinding
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentUsersListBinding
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import ru.mironov.persons_cards_list_viewpager.viewmodel.FragmentUsersListViewModel


@AndroidEntryPoint
class UsersListFragment: Fragment() {

    private val viewModel: FragmentUsersListViewModel by viewModels()

    private var tabLayout: TabLayout? = null
    private var pager2: ViewPager2? = null

    private var _binding: FragmentUsersListBinding? = null
    private var _bindingDialog: BottomsheetlayoutBinding? = null

    private val binding get() = _binding!!
    private val bindingDialog get() = _bindingDialog!!

    var searchBy:String=""
    var sortBy= SortBy.ALPHABET_SORT

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = binding.tabs
        pager2 = binding.viewPager

        setupObserver()
        setupListeners()

        viewModel.allUsersDepartment = requireContext().getString(R.string.department_all)
        viewModel.getUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        _bindingDialog = BottomsheetlayoutBinding.inflate(inflater, container, false)

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

    private fun setUpViewPager(tabName: Array<String>) {
        val adapter = ViewPagerAdapter(activity!!)
        adapter.tabName = tabName
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
                    viewModel.setSearchParam(searchBy,sortBy)
                    setUpViewPager(status.departments!!)
                }
                is Status.LOADING -> {

                }
                is Status.ERROR -> {
                    Toast.makeText(requireContext(), status.code, Toast.LENGTH_LONG)?.show()
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
        val dialog = Dialog(requireContext())
        _bindingDialog = BottomsheetlayoutBinding.inflate(layoutInflater)

        bindingDialog.radioAlphabetSort.isChecked= SortBy.ALPHABET_SORT==sortBy
        bindingDialog.radioBirthDaySort.isChecked= SortBy.BIRTHDAY_SORT==sortBy

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(bindingDialog.root)

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)

        bindingDialog.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_alphabetSort -> {
                    sortBy= SortBy.ALPHABET_SORT
                    binding.search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_search, 0, R.drawable.ic_baseline_sort_off, 0);

                }
                R.id.radio_birthDaySort -> {
                    sortBy= SortBy.BIRTHDAY_SORT
                    binding.search.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_search, 0,
                        R.drawable.ic_baseline_sort_on, 0);
                }
            }
            viewModel.setSearchParam(searchBy,sortBy)
            dialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.search.removeTextChangedListener(textChangeListener)
        bindingDialog.radioGroup.setOnCheckedChangeListener(null)
        _binding = null
        _bindingDialog=null
    }
}