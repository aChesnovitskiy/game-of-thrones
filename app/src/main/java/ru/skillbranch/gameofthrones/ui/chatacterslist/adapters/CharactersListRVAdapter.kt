package ru.skillbranch.gameofthrones.ui.chatacterslist.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_character.view.*
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.utils.extensions.mergeWithDots
import ru.skillbranch.gameofthrones.utils.extensions.toShortName

/* Adapter for RecyclerView with characters */
class CharactersListAdapter() : RecyclerView.Adapter<ViewHolder>() {
    var characters = listOf<CharacterItem>()

    fun updateData(data: List<CharacterItem>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                characters[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                characters[oldPos] == data[newPos]

            override fun getOldListSize() = characters.size
            override fun getNewListSize() = data.size
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        characters = data
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_character, parent, false
            )
        )

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.itemView.run {
        val name = characters[position].name
        val info = getTitlesAndAliases(characters[position]).mergeWithDots()
        val logo = getLogoByHouse(characters[position].house, resources, context)


        tv_character_name.text = if (name.isNotEmpty()) name else
            context.getString(R.string.information_is_unknown)
        tv_character_info.text = if (info.isNotEmpty()) info else
            context.getString(R.string.information_is_unknown)
        iv_house_logo.setImageDrawable(logo)
    }

    private fun getTitlesAndAliases(character: CharacterItem): MutableList<String> {
        val result = mutableListOf<String>()
        if (character.titles.isNotEmpty()) result.addAll(character.titles)
        if (character.aliases.isNotEmpty()) result.addAll(character.aliases)
        return result
    }

    private fun getLogoByHouse(house: String, resources: Resources, context: Context) =
        when (house) {
            NEED_HOUSES[0].toShortName() -> resources.getDrawable(R.drawable.stark_icon, context.theme)
            NEED_HOUSES[1].toShortName() -> resources.getDrawable(R.drawable.lannister_icon, context.theme)
            NEED_HOUSES[2].toShortName() -> resources.getDrawable(R.drawable.targaryen_icon, context.theme)
            NEED_HOUSES[3].toShortName() -> resources.getDrawable(R.drawable.baratheon_icon, context.theme)
            NEED_HOUSES[4].toShortName() -> resources.getDrawable(R.drawable.greyjoy_icon, context.theme)
            NEED_HOUSES[5].toShortName() -> resources.getDrawable(R.drawable.martel_icon, context.theme)
            NEED_HOUSES[6].toShortName() -> resources.getDrawable(R.drawable.tyrel_icon, context.theme)
            else -> resources.getDrawable(R.drawable.ic_block_black_24dp, context.theme)
        }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)