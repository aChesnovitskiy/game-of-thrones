package ru.skillbranch.gameofthrones.ui.chatacterslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.screen_characters_list.*
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.chatacterslist.adapters.CharactersListFragmentAdapter
import ru.skillbranch.gameofthrones.utils.extensions.toShortName

// TODO add comments ih whole project

class CharactersListScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_characters_list)

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

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews() {
        view_pager_list.adapter = CharactersListFragmentAdapter(this)

        // Change header's color when change page
        view_pager_list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val backgroundColor = getColorByPosition(position)
                toolbar.setBackgroundColor(backgroundColor)
                tab_layout_houses.setBackgroundColor(backgroundColor)
            }
        })

        TabLayoutMediator(tab_layout_houses, view_pager_list) { tab, position ->
            tab.text = NEED_HOUSES[position].toShortName()
        }.attach()
    }

    private fun getColorByPosition(position: Int) =
        when (position) {
            0 -> resources.getColor(R.color.stark_primary, theme)
            1 -> resources.getColor(R.color.lannister_primary, theme)
            2 -> resources.getColor(R.color.targaryen_primary, theme)
            3 -> resources.getColor(R.color.baratheon_primary, theme)
            4 -> resources.getColor(R.color.greyjoy_primary, theme)
            5 -> resources.getColor(R.color.martel_primary, theme)
            6 -> resources.getColor(R.color.tyrel_primary, theme)
            else -> resources.getColor(R.color.color_primary, theme)
        }
}
