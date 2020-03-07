package ru.skillbranch.gameofthrones.utils

import android.graphics.drawable.Drawable
import ru.skillbranch.gameofthrones.App
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.utils.extensions.toShortName

object HouseUtils {
    private val resources = App.applicationResources()
    private val context = App.applicationContext()

    fun getPrimaryColor(house: String) =
        when (house) {
            NEED_HOUSES[0].toShortName() -> resources.getColor(R.color.stark_primary, context.theme)
            NEED_HOUSES[1].toShortName() -> resources.getColor(
                R.color.lannister_primary,
                context.theme
            )
            NEED_HOUSES[2].toShortName() -> resources.getColor(
                R.color.targaryen_primary,
                context.theme
            )
            NEED_HOUSES[3].toShortName() -> resources.getColor(
                R.color.baratheon_primary,
                context.theme
            )
            NEED_HOUSES[4].toShortName() -> resources.getColor(
                R.color.greyjoy_primary,
                context.theme
            )
            NEED_HOUSES[5].toShortName() -> resources.getColor(
                R.color.martel_primary,
                context.theme
            )
            NEED_HOUSES[6].toShortName() -> resources.getColor(R.color.tyrel_primary, context.theme)
            else -> resources.getColor(R.color.color_primary, context.theme)
        }

    fun getAccentColor(house: String) =
        when (house) {
            NEED_HOUSES[0].toShortName() -> resources.getColor(R.color.stark_accent, context.theme)
            NEED_HOUSES[1].toShortName() -> resources.getColor(
                R.color.lannister_accent,
                context.theme
            )
            NEED_HOUSES[2].toShortName() -> resources.getColor(
                R.color.targaryen_accent,
                context.theme
            )
            NEED_HOUSES[3].toShortName() -> resources.getColor(
                R.color.baratheon_accent,
                context.theme
            )
            NEED_HOUSES[4].toShortName() -> resources.getColor(
                R.color.greyjoy_accent,
                context.theme
            )
            NEED_HOUSES[5].toShortName() -> resources.getColor(
                R.color.martel_accent,
                context.theme
            )
            NEED_HOUSES[6].toShortName() -> resources.getColor(R.color.tyrel_accent, context.theme)
            else -> resources.getColor(R.color.color_accent, context.theme)
        }

    fun getLogo(house: String): Drawable =
        when (house) {
            NEED_HOUSES[0].toShortName() -> resources.getDrawable(
                R.drawable.stark_icon,
                context.theme
            )
            NEED_HOUSES[1].toShortName() -> resources.getDrawable(
                R.drawable.lannister_icon,
                context.theme
            )
            NEED_HOUSES[2].toShortName() -> resources.getDrawable(
                R.drawable.targaryen_icon,
                context.theme
            )
            NEED_HOUSES[3].toShortName() -> resources.getDrawable(
                R.drawable.baratheon_icon,
                context.theme
            )
            NEED_HOUSES[4].toShortName() -> resources.getDrawable(
                R.drawable.greyjoy_icon,
                context.theme
            )
            NEED_HOUSES[5].toShortName() -> resources.getDrawable(
                R.drawable.martel_icon,
                context.theme
            )
            NEED_HOUSES[6].toShortName() -> resources.getDrawable(
                R.drawable.tyrel_icon,
                context.theme
            )
            else -> resources.getDrawable(R.drawable.ic_block_black_24dp, context.theme)
        }

    fun getCoastOfArms(house: String): Drawable =
        when (house) {
            NEED_HOUSES[0].toShortName() -> resources.getDrawable(
                R.drawable.stark_coast_of_arms,
                context.theme
            )
            NEED_HOUSES[1].toShortName() -> resources.getDrawable(
                R.drawable.lannister_coast_of_arms,
                context.theme
            )
            NEED_HOUSES[2].toShortName() -> resources.getDrawable(
                R.drawable.targaryen_coast_of_arms,
                context.theme
            )
            NEED_HOUSES[3].toShortName() -> resources.getDrawable(
                R.drawable.baratheon_coast_of_arms,
                context.theme
            )
            NEED_HOUSES[4].toShortName() -> resources.getDrawable(
                R.drawable.greyjoy_coast_of_arms,
                context.theme
            )
            NEED_HOUSES[5].toShortName() -> resources.getDrawable(
                R.drawable.martel_coast_of_arms,
                context.theme
            )
            NEED_HOUSES[6].toShortName() -> resources.getDrawable(
                R.drawable.tyrel_coast_of_arms,
                context.theme
            )
            else -> resources.getDrawable(R.drawable.ic_block_black_24dp, context.theme)
        }
}