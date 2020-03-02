package ru.skillbranch.gameofthrones.utils.extensions

import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.local.entities.Character

fun CharacterRes.toCharacter(): Character = Character(
    id = url.split("/").last(),
    name = name,
    gender = gender,
    culture = culture,
    born = born,
    died = died,
    titles = titles,
    aliases = aliases,
    father = father.split("/").lastOrNull() ?: "",
    mother = mother.split("/").lastOrNull() ?: "",
    spouse = spouse,
    houseId = houseId
)