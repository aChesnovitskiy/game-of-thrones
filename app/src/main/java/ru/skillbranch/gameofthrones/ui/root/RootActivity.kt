package ru.skillbranch.gameofthrones.ui.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_root.*
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.adapters.CharactersListFragmentAdapter
import ru.skillbranch.gameofthrones.utils.extensions.shortName

class RootActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        vp_root.adapter = CharactersListFragmentAdapter(this)

        TabLayoutMediator(tl_houses, vp_root) { tab, position ->
                        tab.text = NEED_HOUSES[position].shortName()
        }.attach()

//        RootRepository.getNeedHouses(NEED_HOUSES[0], TODO delete
//            NEED_HOUSES[1],
//            NEED_HOUSES[2],
//            NEED_HOUSES[3],
//            NEED_HOUSES[4],
//            NEED_HOUSES[5],
//            NEED_HOUSES[6]
//            ) {
//            Toast.makeText(this, "${it.size}", Toast.LENGTH_LONG).show() }

    }
}
