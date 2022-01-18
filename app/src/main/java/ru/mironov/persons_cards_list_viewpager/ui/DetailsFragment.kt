package ru.mironov.persons_cards_list_viewpager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentDetailsBinding
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.util.DateFormatter
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import ru.mironov.persons_cards_list_viewpager.util.PhoneNumberFormatUtil
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    private val binding get() = _binding!!

    lateinit var user: JsonUser

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            user = Gson().fromJson(
                requireArguments().getString(ARG_DETAILS_FRAGMENT)!!,
                JsonUser().javaClass
            )
        }
        if (savedInstanceState != null) {
            user = Gson().fromJson(
                savedInstanceState.getString(ARG_DETAILS_FRAGMENT)!!,
                JsonUser().javaClass
            )
        }
        updateUser(user)
        setBackListener()
    }

    private fun updateUser(user: JsonUser) {
        with(binding) {
            glide.asDrawable()
                .placeholder(R.drawable.ic_time)
                .error(R.drawable.ic_error)
                .load(user.avatarUrl)
                .into(userAvatar)

            userName.text = user.firstName + " " + user.lastName
            userTag.text = user.userTag!!.lowercase()
            userDepartment.text =
                DepartmentNameUtil.getDepartmentName(requireContext(), user.department!!)
            userPhone.text = PhoneNumberFormatUtil.formatNumber(user.phone!!)
            userBirthday.text = DateFormatter.convertDate(user.birthday!!)
            userAge.text = DateFormatter.getAge(user.birthday!!)//года/лет --TODO--
        }
    }

    private fun setBackListener() {
        binding.back.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    companion object {
        const val ARG_DETAILS_FRAGMENT = "ARG_DETAILS_FRAGMENT"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.back.setOnClickListener(null)
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ARG_DETAILS_FRAGMENT, Gson().toJson(user))
        super.onSaveInstanceState(outState)
    }

}