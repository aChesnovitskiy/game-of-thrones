package ru.skillbranch.gameofthrones.ui.splash

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.screen_splash.*
import ru.skillbranch.gameofthrones.AppConfig.NEED_HOUSES
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.ui.chatacterslist.CharactersListScreen

class SplashScreen : AppCompatActivity() {
    companion object {
        private const val LOADING_DELAY = 5000L
        private const val ANIMATION_DURATION = 2000L
        private const val ALPHA_MAX = 0.2f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_splash)

        setAnimation()

        val isNetworkAvailable = checkNetworkAvailable()
        Log.d("My_", "isNetworkAvailable: $isNetworkAvailable")


        // Working with DB
        RootRepository.isNeedUpdate { isNeedUpdate ->
            Log.d("My_", "isNeedUpdate: $isNeedUpdate")
            //Check for need of updating DB
            if (isNeedUpdate) {
                // Get houses and characters data from API and insert it into DB
                if (isNetworkAvailable) {
                    RootRepository.getNeedHouseWithCharacters(*NEED_HOUSES) { needHouseWithCharacters ->
                        RootRepository.insertHouses(needHouseWithCharacters.map { it.first }) {
                            RootRepository.insertCharacters(needHouseWithCharacters.map { it.second }
                                .flatten()) {
                                Log.d("My_", "Finish inserting data into DB")
                                goToCharactersList()
                            }
                        }
                    }
                } else {
                    // If there is no network connection
                    Log.d("My_", "No network connection")
                    Snackbar.make(
                        iv_cover,
                        "Internet unavailable - the application cannot be started. Connect to the Internet and restart the application",
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
            } else {
                // DB has data, so we are waiting some time ("loading") before go further
                Log.d("My_", "Splash loading")
                Handler().postDelayed({ goToCharactersList() }, LOADING_DELAY)
            }
        }

    }

    private fun checkNetworkAvailable(): Boolean {
        // Copy from dmisuvorov github
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            connectivityManager.run {
                getNetworkCapabilities(activeNetwork)?.let { networkCapabilities ->
                    return when {
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                        else -> false
                    }
                }
            }
        }

        connectivityManager.run {
            activeNetworkInfo ?: return false
            return true
        }
    }

    private fun goToCharactersList() {
        Log.d("My_", "Go to characters list")
        startActivity(Intent(this, CharactersListScreen::class.java))
        finish()
    }

    private fun setAnimation() {
        val fadeIn = AlphaAnimation(0f, ALPHA_MAX)
        val fadeOut = AlphaAnimation(ALPHA_MAX, 0f)

        fadeIn.apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    iv_cover.startAnimation(fadeOut)
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }

        fadeOut.apply {
            duration = ANIMATION_DURATION
            interpolator = DecelerateInterpolator()
            fillAfter = true
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    iv_cover.startAnimation(fadeIn)
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }

        iv_cover.startAnimation(fadeIn)
    }
}
