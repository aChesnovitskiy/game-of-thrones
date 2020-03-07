package ru.skillbranch.gameofthrones.ui.chatacterslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.screen_characters_list.*
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.chatacterslist.adapters.CharactersListFragmentAdapter
import ru.skillbranch.gameofthrones.utils.HouseUtils
import ru.skillbranch.gameofthrones.utils.extensions.toShortName

// TODO add comments ih whole project

// TODO CharactersList header animation

// TODO headheader transparency

class CharactersListScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_characters_list)

        initToolbar()
        initViews()
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
                val backgroundColor =
                    HouseUtils.getPrimaryColor(NEED_HOUSES[position].toShortName())
                toolbar.setBackgroundColor(backgroundColor)
                tab_layout_houses.setBackgroundColor(backgroundColor)
            }
        })

        // Set tabs' text
        TabLayoutMediator(tab_layout_houses, view_pager_list) { tab, position ->
            tab.text = NEED_HOUSES[position].toShortName()
        }.attach()
    }
}
