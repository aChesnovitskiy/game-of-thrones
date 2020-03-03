package ru.skillbranch.gameofthrones.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_character.view.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.utils.extensions.mergeWithDots

/* Adapter for RecyclerView with characters */
class CharactersListAdapter : RecyclerView.Adapter<ViewHolder>() {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_character, parent, false
        )
    )

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.itemView.run {
        tv_character_name.text = characters[position].name
        val list = getCharacterTitlesAndAliases(characters[position])
        tv_character_info.text = list.mergeWithDots()
        iv_house_logo.setImageDrawable(resources.getDrawable(R.drawable.baratheon_icon, context.theme))
//        tv_title.text = "Item $position" TODO delete
//        container.setBackgroundResource(colors[position])
    }

    private fun getCharacterTitlesAndAliases(character: CharacterItem): MutableList<String> {
        val result = mutableListOf<String>()
        if (character.titles.isNotEmpty()) result.addAll(character.titles)
        if (character.aliases.isNotEmpty()) result.addAll(character.aliases)
        return result
    }
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)