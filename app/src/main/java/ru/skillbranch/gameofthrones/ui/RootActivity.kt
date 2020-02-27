package ru.skillbranch.gameofthrones.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_root.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.adapters.CharactersFragmentAdapter

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        vp_root.adapter = CharactersFragmentAdapter(this)

        TabLayoutMediator(tl_houses, vp_root) { tab, position ->
            tab.text = RootRepository.getHouses()[position]
        }.attach()
    }
}
