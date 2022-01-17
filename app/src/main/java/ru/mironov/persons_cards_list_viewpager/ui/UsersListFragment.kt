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
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.part_result.view.*
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.Status
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentUsersListBinding
import ru.mironov.persons_cards_list_viewpager.databinding.PartResultBinding
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.ui.recyclerview.UserViewHolder
import ru.mironov.persons_cards_list_viewpager.ui.recyclerview.UsersAdapter
import ru.mironov.persons_cards_list_viewpager.viewmodel.UsersListFragmentViewModel
import java.util.ArrayList

@AndroidEntryPoint
class UsersListFragment : Fragment() {

    private val viewModel: UsersListFragmentViewModel by viewModels()

    private lateinit var adapter: UsersAdapter

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    private var _bindingResult: PartResultBinding? = null
    private val bindingResult get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)

        adapterSetup()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        fun getInstance(tabName: String): UsersListFragment {
            val args = Bundle()
            args.putString(ARGS_KEY_TABNAME, tabName)
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
                    ResultRenderer.renderResult(status,binding.rootLayout.part_result)
                    adapter.sortBy=viewModel.getParams()!!.sortBy
                    adapter.users = status.usersList!!.clone() as ArrayList<JsonUser?>
                }
                is Status.LOADING -> {
                    ResultRenderer.renderResult(status,binding.rootLayout.part_result)
                    //progressbar
                }
                is Status.ERROR -> {
                    ResultRenderer.renderResult(status,binding.rootLayout.part_result)
                    Toast.makeText(this.requireContext(), status.message, Toast.LENGTH_LONG)?.show()
                }
                is Status.EMPTY ->{
                    ResultRenderer.renderResult(status,binding.rootLayout.part_result)

                    adapter.users = ArrayList()
                }
            }
        }
    }

    private fun adapterSetup() {
        adapter = UsersAdapter(object : UsersAdapter.ItemClickListener<UserViewHolder> {
            override fun onClickListener(clickedItem: UserViewHolder) {

                val fragment= DetailsFragment()
                val arguments = Bundle()
                arguments.putString(DetailsFragment.ARG_DETAILS_FRAGMENT, Gson().toJson(adapter.users[clickedItem.adapterPosition]))
                fragment.arguments = arguments
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer,fragment)
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