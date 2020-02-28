package ru.skillbranch.gameofthrones.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.page_characters.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.adapters.CharactersAdapter

class CharactersFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.page_characters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val charactersAdapter = CharactersAdapter()

        with(rv_characters) {
            layoutManager = LinearLayoutManager(activity)
            adapter = charactersAdapter
        }

        arguments?.takeIf { it.containsKey("house") }?.apply {
            val characters = RootRepository.getCharacters(getString("house"))
            if (characters != null) {
                charactersAdapter.updateData(characters)
            }
        }
    }
}