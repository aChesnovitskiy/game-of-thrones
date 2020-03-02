package ru.skillbranch.gameofthrones.ui.root

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.page_characters.*
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.AppConfig.HOUSE
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.adapters.CharactersListAdapter
import ru.skillbranch.gameofthrones.viewmodels.CharacterListViewModel

/* Fragment with characters' RecyclerView */
class CharactersListFragment : Fragment() {
    private lateinit var charactersListAdapter: CharactersListAdapter
    private lateinit var viewModel: CharacterListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.page_characters, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        initViewModel()
    }

    private fun initViews() {
        charactersListAdapter = CharactersListAdapter()

        with(rv_characters) {
            layoutManager = LinearLayoutManager(activity)
            adapter = charactersListAdapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(CharacterListViewModel::class.java)
        var house = ""

        if (arguments != null) {
            requireArguments().getString(HOUSE)?.let { house = it }

        }

        Log.d("My_", "Get required arguments. House: ${house}")
        viewModel.getCharacterItemsFromDB(house)
        viewModel.getCharacterItems(house).observe(viewLifecycleOwner, Observer { charactersListAdapter.updateData(it) })
    }
}
