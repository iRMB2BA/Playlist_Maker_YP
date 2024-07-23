package com.example.playlistmakernewversion.settings.domain.api

interface SettingInteractor {

    fun sharingApp(link: String)
    fun openSupport()
    fun openTermsOfUse()


    fun changeTheme(check: Boolean)
    fun defaultChange(): Boolean


}