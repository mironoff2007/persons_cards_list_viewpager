package ru.mironov.persons_cards_list_viewpager.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.SortBy
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentTabBinding
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.ui.recyclerview.UserViewHolder
import ru.mironov.persons_cards_list_viewpager.ui.recyclerview.UsersAdapter
import ru.mironov.persons_cards_list_viewpager.viewmodel.TabFragmentViewModel
import java.util.ArrayList

@AndroidEntryPoint
class TabFragment : Fragment() {

    private val viewModel: TabFragmentViewModel by viewModels()

    private lateinit var adapter: UsersAdapter

    private var _binding: FragmentTabBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabBinding.inflate(inflater, container, false)

        adapterSetup()

        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var department: String? = null
        if (arguments != null) {
            department = arguments!!.getString(ARGS_KEY_TABNAME, null)
        }

        viewModel.allDepartmentName= requireContext().getString(R.string.department_all)
        viewModel.listenSearchParam(department!!)

        setupObserver()
    }

    companion object {
        val ARGS_KEY_TABNAME = "ARGS_KEY_TABNAME "

        fun getInstance(tabName: String): TabFragment {
            val args = Bundle()
            args.putString(ARGS_KEY_TABNAME, tabName)
            val tabFragment = TabFragment()
            tabFragment.arguments = args
            return tabFragment
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObserver() {
        viewModel.mutableStatus.observe(this) { status ->
            when (status) {

                is Status.DATA -> {
                    adapter.sortBy=viewModel.getParams()!!.sortBy
                    adapter.users = status.usersList!!.clone() as ArrayList<JsonUser?>
                }
                is Status.LOADING -> {

                }
                is Status.ERROR -> {
                    Toast.makeText(this.requireContext(), status.code, Toast.LENGTH_LONG)?.show()
                }
            }
        }
    }

    private fun adapterSetup() {
        adapter = UsersAdapter(object : UsersAdapter.ItemClickListener<UserViewHolder> {
            override fun onClickListener(clickedItem: UserViewHolder) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.activityMainLayout,DetailsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })

        val layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}