package com.example.playlistmakernewversion.utill

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmakernewversion.Creator.Creator
import com.example.playlistmakernewversion.settings.domain.api.SettingInteractor
import com.example.playlistmakernewversion.settings.ui.activity.KEY_PREF
import com.example.playlistmakernewversion.settings.ui.activity.PLAYLIST_PREF

class App : Application() {

    private lateinit var interactor : SettingInteractor

    override fun onCreate() {
        super.onCreate()

        interactor = Creator.provideSettingInteractor(applicationContext)
        val darkTheme = interactor.defaultChange()
        switchTheme(darkTheme)
    }


    private fun switchTheme(check: Boolean) {
        interactor.changeTheme(check)
    }
}

