package ru.mironov.persons_cards_list_viewpager.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductionModule {

    @Singleton
    @Provides
    fun provideString(): String{
        return "This is a PRODUCTION string I'm providing for injection"
    }
}















