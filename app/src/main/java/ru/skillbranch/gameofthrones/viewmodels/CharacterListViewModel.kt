package ru.skillbranch.gameofthrones.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.utils.extensions.toShortName

class CharacterListViewModel : ViewModel() {
    private val characterItems = MutableLiveData<List<CharacterItem>>(listOf())
    private val query = MutableLiveData("")

    // Fill list of characters of certain house for RecyclerView from DB
    fun getCharacterItemsFromDB(house: String) {
        RootRepository.findCharactersByHouseName(house.toShortName()) { characters ->
            characterItems.value = characters.sortedBy { it.name }
            Log.d("My_", "GetCharacterItemsFromDB. CharacterItems size: ${characters.size}")
        }
    }

    // Get list of characters for setup into RecyclerView
    fun getCharacterItems(house: String): LiveData<List<CharacterItem>> {
        val result = MediatorLiveData<List<CharacterItem>>()

        val filterF = {
            val queryStr = query.value!!
            val characterItems = characterItems.value!!

            result.value = if (queryStr.isEmpty()) characterItems
            else characterItems.filter { it.name.contains(queryStr, true) }
        }

        result.addSource(characterItems) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    fun handleSearchQuery(query: String?) {
        this.query.value = query
    }
}