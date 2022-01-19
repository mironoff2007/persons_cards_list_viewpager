package ru.mironov.persons_cards_list_viewpager.ui;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


class ViewPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    var tabNames= emptyArray<String>()

    override fun createFragment(position: Int): Fragment {
        return UsersListFragment.getInstance(tabNames[position])
    }

    override fun getItemCount(): Int {
        return tabNames.size
    }
}

