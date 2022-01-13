package ru.mironov.persons_cards_list_viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment


class TabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        val ARGS_KEY_TABNAME = "ARGS_KEY_TABNAME "

        fun getInstance(tabName: String): TabFragment {
            val args = Bundle()
            args.putString(ARGS_KEY_TABNAME , tabName)
            val tabFragment = TabFragment()
            tabFragment.arguments = args
            return tabFragment
        }
    }


}