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
        val ARGS_KEY = "count"

        fun getInstance(count: Int): TabFragment {
            val args = Bundle()
            args.putInt(ARGS_KEY, count)
            val tabFragment = TabFragment()
            tabFragment.arguments = args
            return tabFragment
        }
    }


}