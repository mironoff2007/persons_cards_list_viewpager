package ru.mironov.persons_cards_list_viewpager.domain.util

object AvatarUrlFaker {
    fun getUrl(): String {
        val baseUrl="https://i.pravatar.cc/400?img="
        return baseUrl+(0..70).random()
    }
}