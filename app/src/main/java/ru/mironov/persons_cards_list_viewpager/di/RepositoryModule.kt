package ru.mironov.persons_cards_list_viewpager.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mironov.persons_cards_list_viewpager.data.Repository
import ru.mironov.persons_cards_list_viewpager.retrofit.UsersApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(retrofit: UsersApi): Repository {
        return Repository(retrofit)
    }
}















