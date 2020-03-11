package ru.skillbranch.gameofthrones.ui.chatacterslist

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
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

class CharactersListScreen : AppCompatActivity() {
    var houseColor = Color.WHITE

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

        val revealAnimation = AlphaAnimation(0f, 1f).apply {
            duration = 1000L
            interpolator = DecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    app_bar.setBackgroundColor(houseColor)
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }

        // Set header's color when change page
        view_pager_list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                houseColor = HouseUtils.getPrimaryColor(NEED_HOUSES[position].toShortName())

                view_reveal.setBackgroundColor(houseColor)
                view_reveal.startAnimation(revealAnimation)
            }
        })

        // Set tabs' text
        TabLayoutMediator(tab_layout_houses, view_pager_list) { tab, position ->
            tab.text = NEED_HOUSES[position].toShortName()
        }.attach()
    }
}
