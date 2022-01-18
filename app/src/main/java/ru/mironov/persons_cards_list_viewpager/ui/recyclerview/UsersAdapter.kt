package ru.mironov.persons_cards_list_viewpager.ui.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.databinding.ItemUserBinding
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.util.DateFormatter
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import java.util.*

class UsersAdapter(
    val listener: ItemClickListener<UserViewHolder>,
) :
    RecyclerView.Adapter<UserViewHolder>(), View.OnClickListener {

    var users: ArrayList<JsonUser?> = ArrayList()

    var sortBy: SortBy? = null
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)

        return UserViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user = users[position]
        with(holder.binding) {
            userName.text = user?.firstName + " " + user?.lastName
            userDepartment.text =
                DepartmentNameUtil.getDepartmentName(userDepartment.context, user?.department!!)
            userTag.text = user.userTag?.lowercase()

            //Show or not birthday according to sort param
            if (sortBy == SortBy.BIRTHDAY_SORT) {
                userBirthday.text = DateFormatter.convertDateWithoutYear(user.birthday!!)
            } else {
                userBirthday.text = ""
            }

        }

        val itemBinding = holder.binding
        itemBinding.root.setOnClickListener { listener.onClickListener(holder) }

    }

    override fun getItemCount(): Int {
        return users.size
    }

    interface ItemClickListener<I> {
        fun onClickListener(item: UserViewHolder) {
        }
    }

    override fun onClick(v: View?) {}
}