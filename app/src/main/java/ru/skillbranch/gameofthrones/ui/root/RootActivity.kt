package ru.skillbranch.gameofthrones.ui.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_root.*
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.adapters.CharactersListFragmentAdapter
import ru.skillbranch.gameofthrones.utils.extensions.toShortName

// TODO add comments ih whole project

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        setSupportActionBar(toolbar)

        vp_root.adapter = CharactersListFragmentAdapter(this)

        // TODO move functional of working with DB to the splash screen
        // Get data from webAPI and insert it into DB
        RootRepository.getNeedHouseWithCharacters(*NEED_HOUSES) { needHouseWithCharacters ->
            RootRepository.insertHouses(needHouseWithCharacters.map { it.first }) {
                RootRepository.insertCharacters(needHouseWithCharacters.map { it.second }.flatten()) {
                    Log.d("My_", "Finish inserting data into DB")
                }
            }
        }

        TabLayoutMediator(tl_houses, vp_root) { tab, position ->
            tab.text = NEED_HOUSES[position].toShortName()
        }.attach()
    }
}
