package com.example.playlistmakernewversion.settings.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmakernewversion.utill.App
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.databinding.ActivitySettingsBinding

const val KEY_PREF = "KEY_PREFERENCES"
const val PLAYLIST_PREF = "DARK_MODE_PREF"

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(PLAYLIST_PREF, MODE_PRIVATE)

        binding.buttonArrowBack.setOnClickListener {
            finish()
        }

        binding.sharingTextView.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharingMessage))
            startActivity(Intent.createChooser(shareIntent, null))
        }

        binding.agreementTextView.setOnClickListener {
            val intentSendUrl = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.agreementUrl))
            )
            startActivity(intentSendUrl)
        }

        binding.sendHelpTextView.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("irombaba@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sendHelpThemeMessage))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sendHelpMessage))
            startActivity(shareIntent)
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            sharedPreferences.edit().putBoolean(KEY_PREF, checked).apply()


        }

    }

}