package com.example.playlistmakernewversion.utill

import android.app.Application
import com.example.playlistmakernewversion.Creator.Creator
import com.example.playlistmakernewversion.settings.domain.api.SettingInteractor

class App : Application() {

    private lateinit var interactor: SettingInteractor

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

