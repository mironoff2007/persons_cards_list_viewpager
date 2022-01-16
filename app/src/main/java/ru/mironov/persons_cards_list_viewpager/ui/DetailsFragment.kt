package ru.mironov.persons_cards_list_viewpager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import ru.mironov.persons_cards_list_viewpager.databinding.FragmentDetailsBinding
import ru.mironov.persons_cards_list_viewpager.retrofit.JsonUser
import ru.mironov.persons_cards_list_viewpager.util.DateFormatter
import ru.mironov.persons_cards_list_viewpager.util.DepartmentNameUtil
import ru.mironov.persons_cards_list_viewpager.util.PhoneNumberFormatUtil

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user:JsonUser= Gson().fromJson(requireArguments().getString(ARG_DETAILS_FRAGMENT)!!,JsonUser().javaClass)

        binding.userName.text=user.firstName+" "+user.lastName
        binding.userTag.text=user.userTag
        binding.userDepartment.text=DepartmentNameUtil.getDepartmentName(requireContext(),user.department!!)
        binding.userPhone.text=PhoneNumberFormatUtil.formatNumber(user.phone!!)
        binding.userBirthday.text=DateFormatter.convertDate(user.birthday!!)
        binding.userAge.text=DateFormatter.getAge(user.birthday!!)//года/лет --TODO--

    }

    companion object{
        const val ARG_DETAILS_FRAGMENT="ARG_DETAILS_FRAGMENT"
    }

}