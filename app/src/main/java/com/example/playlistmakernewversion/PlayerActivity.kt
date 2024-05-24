package com.example.playlistmakernewversion

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmakernewversion.databinding.ActivityPlayerBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val track = gson.fromJson(intent.getStringExtra("track"), Track::class.java)

        Glide.with(applicationContext)
            .load(track.coverUrl.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.TrackCover)

        binding.toolBar.setNavigationOnClickListener {
            finish()
        }

        binding.tViewTrackTitle.text = track.trackName
        binding.tViewArtistName.text = track.artistName
        binding.tViewDuration.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.tViewTrackDurationValue.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        setTrackInfo(
            track.collectionName,
            binding.tViewTrackAlbumText,
            binding.tViewTrackAlbumValue
        )
        setTrackInfo(
            track.primaryGenreName,
            binding.tViewTrackGenreText,
            binding.tViewTrackGenreValue
        )
        setTrackInfo(
            track.releaseDate.substring(0, 4),
            binding.tViewTrackYearText,
            binding.tViewTrackYearValue
        )
        setTrackInfo(
            track.country,
            binding.tViewTrackCountryText,
            binding.tViewTrackCountryValue
        )
    }

    private fun setTrackInfo(str: String, text: TextView, value: TextView) {

        if (str.isNotEmpty()) {
            value.text = str
        } else {
            text.isVisible = false
            value.isVisible = false
        }
    }
}