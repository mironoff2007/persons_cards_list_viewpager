package ru.mironov.persons_cards_list_viewpager.mocks

import retrofit2.Call
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.domain.JsonArrayUsers

class MockRepository : Repository(MockApiError()) {
    override fun getUsers(): Call<JsonArrayUsers> {
        return  MockApiError().getUsers()
    }
}