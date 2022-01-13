package ru.mironov.persons_cards_list_viewpager.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.viewmodel.TabFragmentViewModel

@AndroidEntryPoint
class TabFragment : Fragment() {

    private val viewModel: TabFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var department: String? = null
        if (arguments != null) {
            department = arguments!!.getString(ARGS_KEY_TABNAME, null);
        }
        if (department == requireContext().getString(R.string.department_all)) {
            viewModel.getUsersAll()
        } else {
            viewModel.getUsersByDepartment(department)
        }
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


}