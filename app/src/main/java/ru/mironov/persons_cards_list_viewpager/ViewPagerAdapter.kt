package ru.mironov.persons_cards_list_viewpager;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;



class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {


    lateinit var tabName:Array<String>


    override fun createFragment(position: Int): Fragment {
        return TabFragment.getInstance(position+1)
    }

    override fun getItemCount(): Int {
        return tabName.size
    }
}
