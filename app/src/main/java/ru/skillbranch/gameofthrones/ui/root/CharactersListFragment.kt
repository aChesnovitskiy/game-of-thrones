package ru.skillbranch.gameofthrones.ui.root

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.page_characters.*
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.AppConfig.HOUSE
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.adapters.CharactersListAdapter
import ru.skillbranch.gameofthrones.utils.extensions.dpToPx
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
        var house = ""

        if (arguments != null) {
            requireArguments().getString(HOUSE)?.let { house = it }
        }

        initViews(house)
        initViewModel(house)
    }

    private fun initViews(house: String) {
        charactersListAdapter = CharactersListAdapter(house)
        val customDivider = InsetDrawable(
            resources.getDrawable(R.drawable.divider, activity?.theme),
            requireContext().dpToPx(72).toInt(),
            0,
            requireContext().dpToPx(16).toInt(),
            0
        )
        val divider = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            .also { it.setDrawable(customDivider) }

        with(rv_characters) {
            layoutManager = LinearLayoutManager(activity)
            adapter = charactersListAdapter
            addItemDecoration(divider)
        }
    }

    private fun initViewModel(house: String) {
        viewModel = ViewModelProviders.of(this).get(CharacterListViewModel::class.java)
        Log.d("My_", "Get required arguments. House: $house")
        viewModel.getCharacterItemsFromDB(house)
        viewModel.getCharacterItems(house)
            .observe(viewLifecycleOwner, Observer { charactersListAdapter.updateData(it) })
    }
}
