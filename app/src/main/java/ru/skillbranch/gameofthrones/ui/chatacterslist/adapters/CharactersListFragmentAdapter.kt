package ru.skillbranch.gameofthrones.ui.chatacterslist.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.skillbranch.gameofthrones.AppConfig.HOUSE
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.ui.chatacterslist.CharactersListFragment

/* Adapter for ViewPager2 fragments */
class CharactersListFragmentAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = NEED_HOUSES.size

    override fun createFragment(position: Int): Fragment {
        val fragment = CharactersListFragment()

        // Put house name into Bundle
        fragment.arguments = Bundle().apply {
            putString(HOUSE, NEED_HOUSES[position])
            Log.d("My_", "Fragment created. House: ${NEED_HOUSES[position]}")
        }

        return fragment
    }
}