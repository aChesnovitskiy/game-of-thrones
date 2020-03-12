package ru.skillbranch.gameofthrones.utils.extensions

fun String.toShortName(): String = this.split(" ").dropLastUntil { it == "of" }.last()

fun List<String>.joinWithDots(): String {
    val stringBuilder = StringBuilder()

    for (string in this) {
        if (string.isNotBlank()) stringBuilder.append("$string • ")
    }

    if (stringBuilder.isNotBlank()) stringBuilder.setLength(stringBuilder.length - 3)

    return stringBuilder.toString()
}