package ru.mironov.persons_cards_list_viewpager.util

import android.content.Context
import ru.mironov.persons_cards_list_viewpager.R

object DepartmentNameUtil {

    fun getDepartmentName(context: Context, departmentName: String):String {
        return when (departmentName) {
            context.getString(R.string.department_analytics) -> return context.getString(R.string.department_analytics_name)
            context.getString(R.string.department_android) -> return context.getString(R.string.department_android_name)
            context.getString(R.string.department_back_office) -> return context.getString(R.string.department_back_office_name)
            context.getString(R.string.department_backend) -> return context.getString(R.string.department_backend_name)
            context.getString(R.string.department_design) -> return context.getString(R.string.department_design_name)
            context.getString(R.string.department_frontend) -> return context.getString(R.string.department_frontend_name)
            context.getString(R.string.department_hr) -> return context.getString(R.string.department_hr_name)
            context.getString(R.string.department_ios) -> return context.getString(R.string.department_ios_name)
            context.getString(R.string.department_management) -> return context.getString(R.string.department_management_name)
            context.getString(R.string.department_pr) -> return context.getString(R.string.department_pr_name)
            context.getString(R.string.department_qa) -> return context.getString(R.string.department_qa_name)
            context.getString(R.string.department_support) -> return context.getString(R.string.department_support_name)
            else -> {""}
        }

    }
}