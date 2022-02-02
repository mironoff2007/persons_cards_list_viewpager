package ru.mironov.persons_cards_list_viewpager.ui.recyclerview


import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestManager
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import java.util.ArrayList

abstract class AbstractViewHolder(val binding:ViewBinding ) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(glide: RequestManager, user: JsonUser?, sortBy: SortBy?, users: ArrayList<JsonUser?>)
}