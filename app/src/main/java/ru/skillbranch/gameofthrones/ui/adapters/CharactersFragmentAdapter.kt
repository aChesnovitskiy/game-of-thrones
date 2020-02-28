package ru.skillbranch.gameofthrones.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.CharactersFragment

class CharactersFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = RootRepository.getHouses().size

    override fun createFragment(position: Int): Fragment {
        val fragment = CharactersFragment()
        fragment.arguments = Bundle().apply {
            putString("house", RootRepository.getHouses()[position])
        }
        return fragment
    }

}