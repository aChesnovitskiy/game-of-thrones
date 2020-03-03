package ru.skillbranch.gameofthrones.utils.extensions

import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

fun String.toShortName(): String = this.split(" ").dropLastUntil { it == "of" }.last()

fun List<String>.mergeWithDots(): String {
    var stringBuilder = StringBuilder()

    for (string in this) {
        stringBuilder.append("$string • ")
    }

    stringBuilder.setLength(stringBuilder.length - 3)

    return stringBuilder.toString()
}