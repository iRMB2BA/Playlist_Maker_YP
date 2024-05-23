package com.example.playlistmakernewversion

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class TrackActivity : AppCompatActivity() {
    private val gson = Gson()

    private lateinit var trackCover: ImageView
    private lateinit var trackTitle: TextView
    private lateinit var artistName: TextView
    private lateinit var trackDuration: TextView

    private lateinit var trackDurationText: TextView
    private lateinit var trackAlbumText: TextView
    private lateinit var trackYearText: TextView
    private lateinit var trackGenreText: TextView
    private lateinit var trackCountryText: TextView

    private lateinit var trackDurationValue: TextView
    private lateinit var trackAlbumValue: TextView
    private lateinit var trackYearValue: TextView
    private lateinit var trackGenreValue: TextView
    private lateinit var trackCountryValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)
        val track = gson.fromJson(intent.getStringExtra("track"), Track::class.java)
        // For debug purposes
//        val track = Track(
//            1,
//            "Name",
//            "Artist Name",
//            1000,
//            "404",
//            "", //НЕТУ АЛЬБОМА ТАКИЕ ДЕЛА
//            "1999",
//            "Rock",
//            country = "US")
        trackCover = findViewById(R.id.ivTrackCover)
        trackTitle = findViewById(R.id.tvTrackTitle)
        artistName = findViewById(R.id.tvArtistName)
        trackDuration = findViewById(R.id.tvTrackDuration)

        trackDurationText = findViewById(R.id.tvTrackDurationText)
        trackAlbumText = findViewById(R.id.tvTrackAlbumText)
        trackYearText = findViewById(R.id.tvTrackYearText)
        trackGenreText = findViewById(R.id.tvTrackGenreText)
        trackCountryText = findViewById(R.id.tvTrackCountryText)

        trackDurationValue = findViewById(R.id.tvTrackDurationValue)
        trackAlbumValue = findViewById(R.id.tvTrackAlbumValue)
        trackYearValue = findViewById(R.id.tvTrackYearValue)
        trackGenreValue = findViewById(R.id.tvTrackGenreValue)
        trackCountryValue = findViewById(R.id.tvTrackCountryValue)


        findViewById<Toolbar>(R.id.toolBar).setNavigationOnClickListener {
            finish()
        }


        Glide.with(applicationContext)
            .load(track.coverUrl.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(trackCover)

        trackTitle.text = track.trackName
        artistName.text = track.artistName
        trackDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackDurationValue.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        checkAndSetTrackInformationField(track.collectionName, trackAlbumText, trackAlbumValue)
        checkAndSetTrackInformationField(
            track.releaseDate.substring(0, 4),
            trackYearText,
            trackYearValue
        )
        checkAndSetTrackInformationField(track.primaryGenreName, trackGenreText, trackGenreValue)
        checkAndSetTrackInformationField(
            getCountryName(track.country),
            trackCountryText,
            trackCountryValue
        )
    }

    private fun checkAndSetTrackInformationField(
        str: String,
        itemViewText: TextView,
        itemViewValue: TextView
    ) {
        if (str.isNotEmpty()) {
            itemViewValue.text = str
        } else {
            itemViewText.isVisible = false
            itemViewValue.isVisible = false
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("TrackActivity", "onStop on")
    }

}