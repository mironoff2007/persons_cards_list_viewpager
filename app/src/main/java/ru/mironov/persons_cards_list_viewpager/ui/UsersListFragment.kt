package ru.mironov.persons_cards_list_viewpager.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.part_result.view.*
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.viewmodel.Status
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentUsersListBinding
import ru.mironov.persons_cards_list_viewpager.ui.TabsFragment.Companion.TABS_FRAGMENT_TAG
import ru.mironov.persons_cards_list_viewpager.ui.recyclerview.UserViewHolder
import ru.mironov.persons_cards_list_viewpager.ui.recyclerview.UsersAdapter
import ru.mironov.persons_cards_list_viewpager.viewmodel.UsersListFragmentViewModel
import java.util.ArrayList

@AndroidEntryPoint
class UsersListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: UsersListFragmentViewModel by viewModels()

    private lateinit var adapter: UsersAdapter

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)

        adapterSetup()

        // SwipeRefreshLayout
        swipeRefreshLayout = binding.swipeContainer
        swipeRefreshLayout.setOnRefreshListener(this)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var department: String? = null
        if (arguments != null) {
            department = arguments!!.getString(ARGS_KEY_TAB_NAME, null)
        }

        viewModel.allDepartmentName = requireContext().getString(R.string.department_all)
        viewModel.listenSearchParam(department!!)

        setupObserver()
    }

    companion object {
        const val ARGS_KEY_TAB_NAME = "ARGS_KEY_TAB_NAME "

        fun getInstance(tabName: String): UsersListFragment {
            val args = Bundle()
            args.putString(ARGS_KEY_TAB_NAME, tabName)
            val tabFragment = UsersListFragment()
            tabFragment.arguments = args
            return tabFragment
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObserver() {
        viewModel.mutableStatus.observe(this) { status ->
            when (status) {

                is Status.DATA -> {
                    ResultRenderer.renderResult(status, binding.rootLayout.partResult)
                    adapter.sortBy = viewModel.getParams()!!.sortBy
                    adapter.users = status.usersList!!
                }
                is Status.LOADING -> {
                    ResultRenderer.renderResult(status, binding.rootLayout.partResult)
                }
                is Status.ERROR -> {
                    ResultRenderer.renderResult(status, binding.rootLayout.partResult)
                    Toast.makeText(this.requireContext(), status.message, Toast.LENGTH_LONG)?.show()
                }
                is Status.EMPTY -> {
                    ResultRenderer.renderResult(status, binding.rootLayout.partResult)
                    adapter.users = ArrayList()
                }
            }
        }
    }

    private fun adapterSetup() {
        adapter = UsersAdapter(object : UsersAdapter.ItemClickListener<UserViewHolder> {
            override fun onClickListener(item: UserViewHolder) {

                val fragment = DetailsFragment()
                val arguments = Bundle()
                arguments.putString(
                    DetailsFragment.ARG_DETAILS_FRAGMENT,
                    Gson().toJson(adapter.users[item.adapterPosition])
                )
                fragment.arguments = arguments
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
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

    override fun onRefresh() {
        val fragment = parentFragmentManager.findFragmentByTag(TABS_FRAGMENT_TAG) as TabsFragment
        fragment.update()
        if (binding.swipeContainer.isRefreshing) {
            binding.swipeContainer.isRefreshing = false;
        }
    }
}