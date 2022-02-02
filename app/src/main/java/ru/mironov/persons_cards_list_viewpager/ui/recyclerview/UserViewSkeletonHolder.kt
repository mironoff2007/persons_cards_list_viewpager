package ru.mironov.persons_cards_list_viewpager.ui.recyclerview

import com.bumptech.glide.RequestManager
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.databinding.ItemUserSkeletonBinding
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import java.util.ArrayList

class UserViewSkeletonHolder(private val bindingSkeleton: ItemUserSkeletonBinding ) : AbstractViewHolder(bindingSkeleton) {
    override fun bind(
        glide: RequestManager,
        user: JsonUser?,
        sortBy: SortBy?,
        users: ArrayList<JsonUser?>
    ) {}
}
