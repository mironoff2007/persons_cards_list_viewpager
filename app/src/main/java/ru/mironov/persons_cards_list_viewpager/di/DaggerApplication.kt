package ru.mironov.persons_cards_list_viewpager.di

import android.app.Application
import com.bumptech.glide.RequestManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DaggerApplication : Application(){

    override fun onCreate() {
        super.onCreate()
    }

}