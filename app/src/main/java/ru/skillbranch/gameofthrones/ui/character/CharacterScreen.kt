package ru.skillbranch.gameofthrones.ui.character

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.screen_character.*
import ru.skillbranch.gameofthrones.App
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.utils.HouseUtils

class CharacterScreen : AppCompatActivity() {
    var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_character)

        initToolbar()

        id = getIdFromIntent()

        RootRepository.findCharacterFullById(id) {
            Log.d("M_CharacterScreen", it.toString())
            initViews(it)
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

    private fun initViews(character: CharacterFull) {
        val iIsU = this.getString(R.string.information_is_unknown)
        val name = if (character.name.isNotBlank()) character.name else character.aliases[0]
        val house = character.house
        val primaryColor = HouseUtils.getPrimaryColor(house)
        val accentColor = HouseUtils.getAccentColor(house)
        val coastOfArms = HouseUtils.getCoastOfArms(house)

        collapsing_title.apply {
            title = name
            setContentScrimColor(primaryColor)
            setStatusBarScrimColor(primaryColor)
        }

        app_bar.setBackgroundColor(primaryColor)

        iv_house_coast_of_arm.setImageDrawable(coastOfArms)
        
        tv_words.compoundDrawableTintList = ColorStateList.valueOf(accentColor)
        tv_born.compoundDrawableTintList = ColorStateList.valueOf(accentColor)
        tv_titles.compoundDrawableTintList = ColorStateList.valueOf(accentColor)
        tv_aliases.compoundDrawableTintList = ColorStateList.valueOf(accentColor)

        tv_property_words.text = if (character.words.isNotBlank()) character.words else iIsU
        tv_property_born.text = if (character.born.isNotBlank()) character.born else iIsU
        tv_property_titles.text =
            if (character.titles[0].isNotBlank()) character.titles.joinToString(",\n") else iIsU
        tv_property_aliases.text =
            if (character.aliases[0].isNotBlank()) character.aliases.joinToString(",\n") else iIsU

        if (character.father != null && character.father.id.isNotEmpty()) {
            tv_father.visibility = View.VISIBLE
            btn_father.apply {
                visibility = View.VISIBLE
                setBackgroundColor(primaryColor)
                text = character.father.name
                setOnClickListener { goToCharacterScreen(character.father.id) }
            }
        }

        if (character.mother != null && character.mother.id.isNotEmpty()) {
            tv_mother.visibility = View.VISIBLE
            btn_mother.apply {
                visibility = View.VISIBLE
                setBackgroundColor(primaryColor)
                text = character.mother.name
                setOnClickListener { goToCharacterScreen(character.mother.id) }
            }
        }

        if (character.died.isNotEmpty()) {
            Snackbar.make(
                coordinator,
                "$name - \"Died ${character.died}\"",
                Snackbar.LENGTH_INDEFINITE
            ).show()
        }
    }

    private fun goToCharacterScreen(id: String) {
        val intent = Intent(App.applicationContext(), CharacterScreen::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}
