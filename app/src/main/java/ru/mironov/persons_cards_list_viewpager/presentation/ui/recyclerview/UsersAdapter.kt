package ru.mironov.persons_cards_list_viewpager.presentation.ui.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.mironov.persons_cards_list_viewpager.domain.SortBy
import ru.mironov.persons_cards_list_viewpager.databinding.ItemUserBinding
import ru.mironov.persons_cards_list_viewpager.databinding.ItemUserSkeletonBinding
import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import java.util.*
import javax.inject.Inject

class UsersAdapter(
    private val listener: ItemClickListener<AbstractViewHolder>,
) :
    RecyclerView.Adapter<AbstractViewHolder>(), View.OnClickListener {

    @Inject
    lateinit var glide: RequestManager

    private var _binding: ItemUserBinding? = null

    private val binding get() = _binding!!

    private var _bindingSkeleton: ItemUserSkeletonBinding? = null

    private val bindingSkeleton get() = _bindingSkeleton!!

    var users: ArrayList<JsonUser?> = ArrayList()

    var sortBy: SortBy? = null
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return if (users[position] == null) {
            //skeleton type
            0
        } else {
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> {
                _binding = ItemUserBinding.inflate(inflater, parent, false)
                binding.root.setOnClickListener(this)
                UserViewHolder(binding)
            }
            else -> {
                _bindingSkeleton = ItemUserSkeletonBinding.inflate(inflater, parent, false)
                UserViewSkeletonHolder(bindingSkeleton)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        val user = users[position]
        when (holder.itemViewType) {
            1 -> {
                holder.bind(glide, user, sortBy, users)
                holder.binding.root.setOnClickListener { listener.onClickListener(holder) }
            }
            0 -> {}
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    interface ItemClickListener<I> {
        fun onClickListener(item: AbstractViewHolder) {
        }
    }

    override fun onClick(v: View?) {}

}