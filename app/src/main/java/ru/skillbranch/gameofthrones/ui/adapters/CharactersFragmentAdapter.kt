package ru.skillbranch.gameofthrones.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.skillbranch.gameofthrones.ui.CharactersListScreen

class CharactersFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3 // RootRepository.getHouses().size

    override fun createFragment(position: Int): Fragment {
        val fragment = CharactersListScreen()
        fragment.arguments = Bundle().apply {
//            putString("house", RootRepository.getHouses()[position])
        }
        return fragment
    }

}