package ru.mironov.persons_cards_list_viewpager.ui

import android.os.Bundle
import android.os.Debug
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ru.mironov.persons_cards_list_viewpager.R
import ru.mironov.persons_cards_list_viewpager.databinding.ActivityMainBinding
import ru.mironov.persons_cards_list_viewpager.ui.TabsFragment.Companion.TAG_TABS_FRAGMENT

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding get() = _binding!!
    private var _binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Debug.waitForDebugger()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, TabsFragment(),TAG_TABS_FRAGMENT)
                .commit()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


