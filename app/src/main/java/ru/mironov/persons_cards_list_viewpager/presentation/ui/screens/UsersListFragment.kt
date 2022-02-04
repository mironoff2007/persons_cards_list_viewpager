package ru.mironov.persons_cards_list_viewpager.presentation.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.part_result.view.*
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.domain.Status
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentUsersListBinding
import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import ru.mironov.persons_cards_list_viewpager.presentation.ui.ResultRenderer
import ru.mironov.persons_cards_list_viewpager.presentation.ui.screens.TabsFragment.Companion.TAG_TABS_FRAGMENT
import ru.mironov.persons_cards_list_viewpager.presentation.ui.recyclerview.AbstractViewHolder
import ru.mironov.persons_cards_list_viewpager.presentation.ui.recyclerview.UsersAdapter
import ru.mironov.persons_cards_list_viewpager.presentation.viewmodel.UsersListFragmentViewModel
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class UsersListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: UsersListFragmentViewModel by viewModels()

    private var adapter: UsersAdapter?=null

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var glide: RequestManager

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)

        adapterSetup()
        setupObserver()

        // SwipeRefreshLayout
        swipeRefreshLayout = binding.swipeContainer
        swipeRefreshLayout.setOnRefreshListener(this)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var department: String? = null
        if (arguments != null) {
            department = requireArguments().getString(ARGS_KEY_TAB_NAME, null)
        }

        viewModel.allDepartmentName = requireContext().getString(R.string.department_all)
        viewModel.listenSearchParam(department!!)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.post {
           binding.recyclerView.layoutManager?.scrollToPosition(viewModel.position)
        }

        binding.progressBar.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupObserver() {
        viewModel.mutableStatus.observe(viewLifecycleOwner) { status ->
            when (status) {

                is Status.DATA -> {
                    binding.progressBar.visibility = View.GONE
                    ResultRenderer.renderResult(status, binding.rootLayout.partResult)
                    adapter!!.sortBy = viewModel.getParams()!!.sortBy
                    adapter!!.users = status.usersList!!
                }
                is Status.LOADING -> {
                    ResultRenderer.renderResult(status, binding.rootLayout.partResult)
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Status.EMPTY -> {
                    binding.progressBar.visibility = View.GONE
                    ResultRenderer.renderResult(status, binding.rootLayout.partResult)
                    adapter!!.users = ArrayList()
                }
            }
        }
    }

    private fun adapterSetup() {
        adapter = UsersAdapter(object : UsersAdapter.ItemClickListener<AbstractViewHolder> {
            override fun onClickListener(item: AbstractViewHolder) {

                viewModel.position=item.adapterPosition

                val fragment = DetailsFragment()
                val argumentsDetails = Bundle()
                argumentsDetails.putString(
                    DetailsFragment.ARG_DETAILS_FRAGMENT,
                    Gson().toJson(adapter!!.users[item.adapterPosition])
                )

                fragment.arguments = argumentsDetails

                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })

        adapter!!.glide=glide

        //For skeletons
        adapter!!.users=arrayListOf<JsonUser?>(null,null,null,null,null,null,null,null,null,null)

        val layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, 0))
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onRefresh() {

        binding.recyclerView.post {
            binding.recyclerView.layoutManager?.scrollToPosition(viewModel.position)
        }

        val fragment = requireActivity().supportFragmentManager.findFragmentByTag(TAG_TABS_FRAGMENT) as TabsFragment
        fragment.update()
        if (binding.swipeContainer.isRefreshing) {
            binding.swipeContainer.isRefreshing = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        swipeRefreshLayout.setOnRefreshListener(null)
        adapter=null
        _binding = null
    }

    companion object {

        const val ARGS_KEY_TAB_NAME = "ARGS_KEY_TAB_NAME"

        fun getInstance(tabName: String): UsersListFragment {

                val args = Bundle()
                args.putString(ARGS_KEY_TAB_NAME, tabName)
                val fragment = UsersListFragment()
                fragment.arguments = args

            return fragment
        }
    }
}