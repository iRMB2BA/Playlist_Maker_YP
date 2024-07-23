package com.example.playlistmakernewversion.settings.domain.api

interface SettingRepository {

    fun sharingApp(link: String)

    fun openSupport()

    fun openTermsOfUse()

    fun themeChange(check: Boolean)
    fun defaultChange(): Boolean
}