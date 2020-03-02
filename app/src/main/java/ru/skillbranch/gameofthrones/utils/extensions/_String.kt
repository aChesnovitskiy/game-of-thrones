package ru.skillbranch.gameofthrones.utils.extensions

import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

fun String.toShortName(): String {
    return this.split(" ").dropLastUntil { it == "of" }.last()
}