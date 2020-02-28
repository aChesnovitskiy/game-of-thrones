package ru.skillbranch.gameofthrones.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.achesnovitskiy.empoyees.api.ApiFactory
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_root.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes
import ru.skillbranch.gameofthrones.ui.adapters.CharactersFragmentAdapter

class RootActivity : AppCompatActivity() {

    var list = mutableListOf<HouseRes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        vp_root.adapter = CharactersFragmentAdapter(this)

        TabLayoutMediator(tl_houses, vp_root) { tab, position ->
//            tab.text = RootRepository.getHouses()[position]
        }.attach()

        val apiFactory = ApiFactory
        val apiService = apiFactory.getApiService()
        for (i in 0..3) {
            val disposable: Disposable = apiService.getHouses(i)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        result ->
                    run {
                        Toast.makeText(this, result[0].name, Toast.LENGTH_SHORT).show()
                        for (houseRes in result) {
                            list.add(houseRes)
                        }
                        Toast.makeText(this, "${list.size}", Toast.LENGTH_SHORT).show()

                    }
                }, { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                })
        }
    }
}
