package com.example.playlistmakernewversion.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmakernewversion.media.MediaActivity
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.search.SearchActivity
import com.example.playlistmakernewversion.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonSettings = findViewById<Button>(R.id.button_settings)

        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        val buttonMedia = findViewById<Button>(R.id.button_media)

        buttonMedia.setOnClickListener {
            val intent = Intent(this, MediaActivity::class.java)
            startActivity(intent)
        }

        val buttonSearch = findViewById<Button>(R.id.button_search)

        buttonSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}