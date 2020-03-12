package ru.skillbranch.gameofthrones.ui.chatacterslist

import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.screen_characters_list.*
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.chatacterslist.adapters.CharactersListFragmentAdapter
import ru.skillbranch.gameofthrones.utils.HouseUtils
import ru.skillbranch.gameofthrones.utils.extensions.toShortName
import kotlin.math.hypot
import kotlin.math.max

class CharactersListScreen : AppCompatActivity() {

    private var houseColor = Color.WHITE

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

        // Set tabs' text
        TabLayoutMediator(tab_layout_houses, view_pager_list) { tab, position ->
            tab.text = NEED_HOUSES[position].toShortName()
        }.attach()

        // Set header's color and animation when change page
        view_pager_list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                houseColor = HouseUtils.getPrimaryColor(NEED_HOUSES[position].toShortName())

                val tabCoordinateX = getTabCoordinateX(position)

                setHeaderAnimation(tabCoordinateX)
            }
        })
    }

    private fun getTabCoordinateX(position: Int): Int {
        // Variant from Skill-Branch summary
        val rect = Rect()
        val tab = tab_layout_houses.getTabAt(position)?.view as View
        tab.getGlobalVisibleRect(rect)
        return rect.centerX()

        // My variant - problems:
        // 1) Get beginning of tab, not center
        // 2) Sometimes cause wrong radius calculation
//        val tab = (tab_layout_houses.getChildAt(0) as ViewGroup).getChildAt(position)
//        val coordinates = intArrayOf(0, 0)
//        tab.getLocationInWindow(coordinates)
//        return coordinates[0]
    }

    private fun setHeaderAnimation(centerX: Int) {
        val centerY = app_bar.height
        val endRadius = radiusCalculation(centerX)

        view_reveal.apply {
            setBackgroundColor(houseColor)
            view_reveal.visibility = View.VISIBLE
        }

        ViewAnimationUtils.createCircularReveal(
            view_reveal,
            centerX, centerY, 0F, endRadius
        )
            .apply {
                interpolator = DecelerateInterpolator()
                doOnEnd {
                    view_reveal.visibility = View.INVISIBLE
                    app_bar.setBackgroundColor(houseColor)
                }
            }.start()
    }

    private fun radiusCalculation(centerX: Int): Float {
        val height = app_bar.height.toFloat()
        val radiusLeft = hypot(centerX.toFloat(), height)
        val radiusRight = hypot((app_bar.width - centerX).toFloat(), height)
        return max(radiusLeft, radiusRight)
    }
}
