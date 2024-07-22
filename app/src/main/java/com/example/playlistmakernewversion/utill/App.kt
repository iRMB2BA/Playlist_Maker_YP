package com.example.playlistmakernewversion.utill

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmakernewversion.settings.ui.activity.KEY_PREF
import com.example.playlistmakernewversion.settings.ui.activity.PLAYLIST_PREF

class App : Application() {
    private var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(PLAYLIST_PREF, MODE_PRIVATE)
        switchTheme(sharedPreferences.getBoolean(KEY_PREF, darkTheme))
    }


    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}

