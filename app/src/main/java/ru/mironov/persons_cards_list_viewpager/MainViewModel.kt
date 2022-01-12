package ru.mironov.persons_cards_list_viewpager

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel

class MainViewModel @Inject constructor (val repository:Repository): ViewModel(){
}