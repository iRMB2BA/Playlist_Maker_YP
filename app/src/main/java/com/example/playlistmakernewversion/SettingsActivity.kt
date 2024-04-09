package com.example.playlistmakernewversion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonBack = findViewById<ImageView>(R.id.button_arrowBack)
        val buttonSharing = findViewById<TextView>(R.id.sharing_textView)
        val buttonAgreement = findViewById<TextView>(R.id.agreement_textView)
        val buttonSendHelp = findViewById<TextView>(R.id.sendHelp_textView)

        buttonBack.setOnClickListener {
            finish()
        }

        buttonSharing.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharingMessage))
            startActivity(Intent.createChooser(shareIntent, null))
        }

        buttonAgreement.setOnClickListener {
            val intentSendUrl = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.agreementUrl))
            )
            startActivity(intentSendUrl)
        }

        buttonSendHelp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("irombaba@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sendHelpThemeMessage))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sendHelpMessage))
            startActivity(shareIntent)
        }

    }

}