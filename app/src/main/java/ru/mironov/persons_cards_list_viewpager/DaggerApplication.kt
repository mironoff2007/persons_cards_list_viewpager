package ru.mironov.persons_cards_list_viewpager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DaggerApplication : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}