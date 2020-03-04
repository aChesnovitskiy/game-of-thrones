package ru.skillbranch.gameofthrones.ui.chatacterslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_characters_list.*
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.chatacterslist.adapters.CharactersListFragmentAdapter
import ru.skillbranch.gameofthrones.utils.extensions.toShortName
import ru.skillbranch.gameofthrones.viewmodels.CharacterListViewModel

// TODO add comments ih whole project

class CharactersListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_characters_list)

        initToolbar()
        initViews()

        // TODO move functional of working with DB to the splash screen
        // Get data from webAPI and insert it into DB
        RootRepository.getNeedHouseWithCharacters(*NEED_HOUSES) { needHouseWithCharacters ->
            RootRepository.insertHouses(needHouseWithCharacters.map { it.first }) {
                RootRepository.insertCharacters(needHouseWithCharacters.map { it.second }.flatten()) {
                    Log.d("My_", "Finish inserting data into DB")
                }
            }
        }
    }

    private fun initViews() {
        vp_root.adapter = CharactersListFragmentAdapter(this)


        TabLayoutMediator(tl_houses, vp_root) { tab, position ->
            tab.text = NEED_HOUSES[position].toShortName()
        }.attach()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }
}
