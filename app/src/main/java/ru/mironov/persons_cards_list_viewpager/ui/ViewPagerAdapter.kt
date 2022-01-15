package ru.mironov.persons_cards_list_viewpager.ui;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    lateinit var tabName:Array<String>

    override fun createFragment(position: Int): Fragment {
        return TabFragment.getInstance(tabName[position])
    }

    override fun getItemCount(): Int {
        return tabName.size
    }
}

