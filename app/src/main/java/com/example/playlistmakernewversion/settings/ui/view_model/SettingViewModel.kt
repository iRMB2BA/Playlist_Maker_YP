package com.example.playlistmakernewversion.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmakernewversion.Creator.Creator

class SettingViewModel(application: Application) : AndroidViewModel(application) {

    private val settingInteractor = Creator.provideSettingInteractor(application)


    fun sentSupport() {
        settingInteractor.openSupport()
    }

    fun shareApp(link: String) {
        settingInteractor.sharingApp(link)
    }

    fun termOfUse() {
        settingInteractor.openTermsOfUse()
    }


    fun default(): Boolean {
        return settingInteractor.defaultChange()
    }

    fun switchTheme(check: Boolean) {
        settingInteractor.changeTheme(check)
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY]
                SettingViewModel(application!!)
            }
        }
    }
}