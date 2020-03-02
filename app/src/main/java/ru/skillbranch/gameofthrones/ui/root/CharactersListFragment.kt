package ru.skillbranch.gameofthrones.ui.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.page_characters.*
import ru.skillbranch.gameofthrones.AppConfig.HOUSE
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.adapters.CharactersListAdapter
import ru.skillbranch.gameofthrones.viewmodels.CharacterListViewModel

/* Fragment with characters' RecyclerView */
class CharactersListFragment : Fragment() {
    private val charactersListAdapter = CharactersListAdapter()
    private val viewModel = CharacterListViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.page_characters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            val house = requireArguments().getString(HOUSE)

            viewModel.getCharacterItemsFromDB(house)

            with(rv_characters) {
                layoutManager = LinearLayoutManager(activity)
                adapter = charactersListAdapter
            }
            charactersListAdapter.updateData(listOf("" to ""))
        }
    }
}
