package ru.skillbranch.gameofthrones.ui.character

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.screen_character.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.repositories.RootRepository

class CharacterScreen : AppCompatActivity() {
    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_character)

        initToolbar()
        id = getIdFromIntent()
        RootRepository.findCharacterFullById(id) {
            initView(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getIdFromIntent(): String {
        val intentFromList = intent
        return if (intentFromList != null && intentFromList.hasExtra("id")) {
            intentFromList.getStringExtra("id")
        } else {
            finish()
            ""
        }
    }

    private fun initView(character: CharacterFull) {
        val iIsU = this.getString(R.string.information_is_unknown)

        tv_property_words.text = if (character.words.isNotBlank()) character.words else iIsU
        tv_property_born.text = if (character.born.isNotBlank()) character.born else iIsU
        tv_property_titles.text =
            if (character.titles[0].isNotBlank()) character.titles.joinToString(",\n") else iIsU
        tv_property_aliases.text =
            if (character.aliases[0].isNotBlank()) character.aliases.joinToString(",\n") else iIsU

        if (character.father != null) {
            tv_father.visibility = View.VISIBLE
            btn_father.visibility = View.VISIBLE
        }

        if (character.mother != null) {
            tv_mother.visibility = View.VISIBLE
            btn_mother.visibility = View.VISIBLE
        }
    }
}
