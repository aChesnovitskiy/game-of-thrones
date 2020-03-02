package ru.skillbranch.gameofthrones.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.repositories.RootRepository

class CharacterListViewModel {
    private val rootRepository = RootRepository
    private val characterItems = MutableLiveData<List<CharacterItem>>()

    fun getCharacterItemsFromDB(house: String) {
        rootRepository.findCharactersByHouseName(house) { characters ->
            characterItems.value = characters.sortedBy { it.name }
        }
    }

//    fun getCharacterItems(house: String): LiveData<List<CharacterItem>> {
//        val result = listOf<CharacterItem>()
//
//        return result
//    }

}