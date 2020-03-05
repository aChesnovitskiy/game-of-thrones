package ru.skillbranch.gameofthrones.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import ru.skillbranch.gameofthrones.AppConfig.BASE_URL
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.chatacterslist.CharactersListScreen
import java.lang.Exception
import java.net.InetAddress

class SplashScreen : AppCompatActivity() {
    companion object {
        private const val ACTION_NO_INTERNET = 0
        private const val ACTION_UPDATE_DB = 1
        private const val ACTION_LOADING = 2

        private const val LOADING_DELAY = 5000L
    }

    // TODO add animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_splash)

        var isNeedUpdate = false
        RootRepository.isNeedUpdate { isNeedUpdate = it }

        val isInternetAvailable = isInternetAvailable()

        when (getSplashAction(isNeedUpdate, isInternetAvailable)) {
            ACTION_NO_INTERNET ->
                ACTION_UPDATE_DB
            ->
                ACTION_LOADING
            ->
        }

        Handler().postDelayed({
            startActivity(Intent(this, CharactersListScreen::class.java))
            finish()
        }, LOADING_DELAY)
    }

    private fun isInternetAvailable(): Boolean = try {
        !InetAddress.getByName(BASE_URL).equals("")
    } catch (e: Exception) {
        false
    }

    private fun getSplashAction(isNeedUpdate: Boolean, isInternetAvailable: Boolean) = when {
        isNeedUpdate && !isInternetAvailable -> ACTION_NO_INTERNET
        isNeedUpdate && isInternetAvailable -> ACTION_UPDATE_DB
        else -> ACTION_LOADING
    }
}
