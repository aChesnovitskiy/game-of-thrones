package ru.skillbranch.gameofthrones.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_character.view.*
import kotlinx.android.synthetic.main.page_characters.view.*
import ru.skillbranch.gameofthrones.R

class CharactersAdapter : RecyclerView.Adapter<PagerVH>() {
//    private val colors = intArrayOf(
//        android.R.color.black,
//        android.R.color.holo_red_light,
//        android.R.color.holo_blue_dark,
//        android.R.color.holo_purple
//    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH = PagerVH(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_character, parent, false
        )
    )

    override fun getItemCount(): Int = 3 // TODO

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        tv_character_name.text = "Character $position"
        tv_character_info.text = "Info $position"
//        tv_title.text = "Item $position"
//        container.setBackgroundResource(colors[position])
    }

}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)