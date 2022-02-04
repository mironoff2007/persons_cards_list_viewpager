package ru.mironov.persons_cards_list_viewpager.presentation.ui.recyclerview

import com.bumptech.glide.RequestManager
import ru.mironov.persons_cards_list_viewpager.domain.SortBy
import ru.mironov.persons_cards_list_viewpager.databinding.ItemUserSkeletonBinding
import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import java.util.ArrayList

class UserViewSkeletonHolder(private val bindingSkeleton: ItemUserSkeletonBinding ) : AbstractViewHolder(bindingSkeleton) {
    override fun bind(
        glide: RequestManager,
        user: JsonUser?,
        sortBy: SortBy?,
        users: ArrayList<JsonUser?>
    ) {}
}
