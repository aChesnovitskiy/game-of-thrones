package ru.skillbranch.gameofthrones.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.ui.CharactersListScreenFragment

class CharactersListScreenFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = NEED_HOUSES.size

    override fun createFragment(position: Int): Fragment {
        val fragment = CharactersListScreenFragment()
        fragment.arguments = Bundle().apply {
//            putString("house", RootRepository.getHouses()[position]) TODO
        }
        return fragment
    }

}