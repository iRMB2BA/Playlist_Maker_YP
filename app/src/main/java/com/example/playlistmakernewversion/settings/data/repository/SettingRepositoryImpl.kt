package com.example.playlistmakernewversion.settings.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.settings.data.SharedPreferencesThemeSettings
import com.example.playlistmakernewversion.settings.domain.api.SettingRepository

class SettingRepositoryImpl(
    private val context: Context,
    private val sharedPreferencesThemeSettings: SharedPreferencesThemeSettings
) :
    SettingRepository {

    override fun sharingApp(link: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openSupport() {

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            val email = context.getString(R.string.adress)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.sendHelpThemeMessage))
            putExtra(Intent.EXTRA_TEXT, context.getString(R.string.sendHelpMessage))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openTermsOfUse() {
        val link = "https://yandex.ru/legal/practicum_offer/"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun themeChange(check: Boolean) {
        sharedPreferencesThemeSettings.switchTheme(check)
    }

    override fun defaultChange(): Boolean {
        return sharedPreferencesThemeSettings.valueChangeDefault()
    }
}