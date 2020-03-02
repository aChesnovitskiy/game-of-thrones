package ru.skillbranch.gameofthrones.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.utils.extensions.toShortName

class CharacterListViewModel : ViewModel() {
    private val characterItems = MutableLiveData<List<CharacterItem>>()

    fun getCharacterItemsFromDB(house: String) {
        RootRepository.findCharactersByHouseName(house.toShortName()) { characters ->
            characterItems.value = characters.sortedBy { it.name }
            Log.d("My_", "GetCharacterItemsFromDB. CharacterItems size: ${characters.size}")
        }
    }

    fun getCharacterItems(house: String): LiveData<List<CharacterItem>> = characterItems
}