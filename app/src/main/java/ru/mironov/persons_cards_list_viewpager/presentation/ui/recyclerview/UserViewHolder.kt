package ru.mironov.persons_cards_list_viewpager.presentation.ui.recyclerview

import android.annotation.SuppressLint
import android.view.View
import com.bumptech.glide.RequestManager
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.domain.SortBy
import ru.mironov.persons_cards_list_viewpager.databinding.ItemUserBinding
import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import ru.mironov.persons_cards_list_viewpager.domain.util.DateFormatter
import ru.mironov.persons_cards_list_viewpager.domain.util.DepartmentNameUtil
import java.util.ArrayList

class UserViewHolder(private val bindingUser: ItemUserBinding ) : AbstractViewHolder(bindingUser){

    @SuppressLint("SetTextI18n")
    override fun bind(glide: RequestManager, user: JsonUser?, sortBy: SortBy?, users: ArrayList<JsonUser?>){
        with(bindingUser) {

            glide.asDrawable()
                .placeholder(R.drawable.ic_time)
                .error(R.drawable.ic_error)
                .load(user?.avatarUrl)
                .into(userAvatar)

            userName.text = user?.firstName + " " + user?.lastName
            userDepartment.text =
                DepartmentNameUtil.getDepartmentName(
                    userDepartment.context,
                    user?.department!!
                )
            userTag.text = user.userTag?.lowercase()

            //Show or not birthday according to sort param
            if (sortBy == SortBy.BIRTHDAY_SORT) {
                userBirthday.text = DateFormatter.convertDateWithoutYear(user.birthday!!)

                //Draw year divider
                if (users.size > position + 1) {
                    val yearPrev = DateFormatter.getYear(users[position]?.birthday)
                    val yearNext = DateFormatter.getYear(users[position + 1]?.birthday)
                    if (yearNext > yearPrev) {
                        yearRow.visibility = View.VISIBLE
                        yearText.text = yearPrev.toString()
                    } else {
                        yearRow.visibility = View.GONE
                    }
                } else {
                    yearRow.visibility = View.GONE
                }
            } else {
                userBirthday.text = ""
                yearRow.visibility = View.GONE
            }
        }
    }
}
