package ru.skillbranch.gameofthrones.repositories

object RootRepository {
    private val houses = arrayOf("Stark", "Lannister", "Targaryen", "Baratheon", "Greyjoy", "Martel", "Tyrell")
    private var characters = mutableMapOf<String, List<Pair<String, String>>>()

    private val starks = listOf(
        "Stark 1" to "Starkers1",
        "Stark 2" to "Starkers2",
        "Stark 3" to "Starkers3",
        "Stark 4" to "Starkers4"
    )
    private val lannisters = listOf(
        "Lannister 1" to "Lannisterers1",
        "Lannister 2" to "Lannisterers2",
        "Lannister 3" to "Lannisterers3"
    )
    private val targaryens = listOf(
        "Targaryen 1" to "Targaryeners1",
        "Targaryen 2" to "Targaryeners2",
        "Targaryen 3" to "Targaryeners3",
        "Targaryen 4" to "Targaryeners4",
        "Targaryen 5" to "Targaryeners5"
    )
    private val baratheons = listOf(
        "Baratheon 1" to "Baratheoners1",
        "Baratheon 2" to "Baratheoners2",
        "Baratheon 3" to "Baratheoners3"
    )
    private val greyjoys = listOf(
        "Greyjoy 1" to "Greyjoyers1",
        "Greyjoy 2" to "Greyjoyers2",
        "Greyjoy 3" to "Greyjoyers3"
    )
    private val martels = listOf(
        "Martel 1" to "Martelers1",
        "Martel 2" to "Martelers2",
        "Martel 3" to "Martelers3"
    )
    private val tyrells = listOf(
        "Tyrell 1" to "Tyrellers1",
        "Tyrell 2" to "Tyrellers2",
        "Tyrell 3" to "Tyrellers3"
    )
    init {
        characters.put("Stark", starks)
        characters.put("Lannister", lannisters)
        characters.put("Targaryen", targaryens)
        characters.put("Baratheon", baratheons)
        characters.put("Greyjoy", greyjoys)
        characters.put("Martel", martels)
        characters.put("Tyrell", tyrells)
    }


    fun getHouses() = houses

    fun getCharacters(house: String?) = characters[house]
}