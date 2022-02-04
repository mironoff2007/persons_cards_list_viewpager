package ru.mironov.persons_cards_list_viewpager.domain.util

import ru.mironov.persons_cards_list_viewpager.domain.JsonUser
import ru.mironov.persons_cards_list_viewpager.domain.SortParams
import java.util.ArrayList

object SearchUtil {

    fun search(list:ArrayList<JsonUser?>?,department: String, allDepartmentName:String , params: SortParams): ArrayList<JsonUser?>? {
       return list?.filter { user ->
            (user?.department == department || department == allDepartmentName) &&
                    ((user?.firstName + " " + user?.lastName).lowercase()
                        .contains(params.searchBy.lowercase()) ||
                            user?.userTag?.lowercase()?.contains(params.searchBy) == true ||
                            params.searchBy.isEmpty())
        } as ArrayList<JsonUser?>?
    }
}