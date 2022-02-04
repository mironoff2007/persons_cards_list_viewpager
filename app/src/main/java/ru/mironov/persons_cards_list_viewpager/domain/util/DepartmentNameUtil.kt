package ru.mironov.persons_cards_list_viewpager.domain.util

import android.content.Context
import ru.mironov.persons_cards_list_viewpager.R

object DepartmentNameUtil {

    fun getDepartmentName(context: Context, departmentName: String):String {
            val ind=context.resources.getStringArray(R.array.department_names_api).indexOf(departmentName)
        return  context.resources.getStringArray(R.array.department_names_ui)[ind]
    }
}