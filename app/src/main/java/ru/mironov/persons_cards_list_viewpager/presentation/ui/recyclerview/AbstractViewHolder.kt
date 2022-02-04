package ru.mironov.persons_cards_list_viewpager.presentation.ui.recyclerview


import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.RequestManager
import ru.mironov.persons_cards_list_viewpager.domain.SortBy
import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import java.util.ArrayList

abstract class AbstractViewHolder(val binding:ViewBinding ) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(glide: RequestManager, user: JsonUser?, sortBy: SortBy?, users: ArrayList<JsonUser?>)
}