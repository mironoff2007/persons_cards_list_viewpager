package ru.mironov.persons_cards_list_viewpager.ui.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.data.SortBy
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentDetailsBinding
import ru.mironov.persons_cards_list_viewpager.databinding.ItemUserBinding
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.util.DateFormatter
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import java.util.*
import javax.inject.Inject

class UsersAdapter(
    private val listener: ItemClickListener<UserViewHolder>,
) :
    RecyclerView.Adapter<UserViewHolder>(), View.OnClickListener {

    @Inject
    lateinit var glide: RequestManager

    private var _binding: ItemUserBinding? = null

    private val binding get() = _binding!!

    var users: ArrayList<JsonUser?> = ArrayList()

    var sortBy: SortBy? = null
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        _binding = ItemUserBinding.inflate(inflater, parent, false)
        binding.root.setOnClickListener(this)

        return UserViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user = users[position]

        with(holder.binding) {

            glide.asDrawable()
                .placeholder(R.drawable.ic_time)
                .error(R.drawable.ic_error)
                .load(user?.avatarUrl)
                .into(userAvatar)

            userName.text = user?.firstName + " " + user?.lastName
            userDepartment.text =
                DepartmentNameUtil.getDepartmentName(userDepartment.context, user?.department!!)
            userTag.text = user.userTag?.lowercase()

            //Show or not birthday according to sort param
            if (sortBy == SortBy.BIRTHDAY_SORT) {
                userBirthday.text = DateFormatter.convertDateWithoutYear(user.birthday!!)

                //Draw year divider
                if(users.size>position+1){
                    val yearPrev=DateFormatter.getYear(users[position]?.birthday)
                    val yearNext=DateFormatter.getYear(users[position+1]?.birthday)
                    if(yearNext>yearPrev){
                        yearRow.visibility=View.VISIBLE
                        yearText.text=yearPrev.toString()
                    }
                    else{
                        yearRow.visibility=View.GONE
                    }
                }
                else{
                    yearRow.visibility=View.GONE
                }
            } else {
                userBirthday.text = ""
                yearRow.visibility=View.GONE
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