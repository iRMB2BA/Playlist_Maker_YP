package com.example.playlistmakernewversion.ui.player

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.creator.Creator
import com.example.playlistmakernewversion.databinding.ActivityPlayerBinding
import com.example.playlistmakernewversion.domain.api.TrackStateListener
import com.example.playlistmakernewversion.domain.api.TrackTimeListener
import com.example.playlistmakernewversion.domain.models.Track
import com.example.playlistmakernewversion.ui.search.KEY_TRACK_INTENT
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity(), TrackTimeListener, TrackStateListener {

    private val playerInteractor = Creator.providePlayerInteractor(this, this)
    val handler = Handler(Looper.getMainLooper())
    var state = PlayerState.STATE_DEFAULT.state
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private var btEnabled = false


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        binding.toolBar.setOnClickListener {
            finish()
        }

        track = intent.getParcelableExtra(KEY_TRACK_INTENT, Track::class.java)!!

        binding.btnPlayStop.setOnClickListener {
            playerInteractor.playbackControl()
            checkState(state)
        }

        playerInteractor.preparePlayer(track.previewUrl)

        listenState()

        Glide.with(applicationContext)
            .load(track.coverUrl.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(binding.TrackCover)

        binding.tViewTrackTitle.text = track.trackName
        binding.tViewArtistName.text = track.artistName
        binding.tViewTrackDurationValue.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(track.trackTimeMillis)


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

        val releaseDate = if (track.releaseDate.isBlank()) "" else track.releaseDate.substring(0, 4)
        setTrackInfo(
            releaseDate,
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

    override fun onPause() {
        super.onPause()
        playerInteractor.pausePlayer()
        binding.btnPlayStop.setImageResource(R.drawable.ic_play)
        btEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.releasePlayer()
    }


    override fun getState(state: Int) {
        this.state = state
    }

    override fun onTimeChanged(time: String) {
        binding.tViewDuration.text = time
    }

    fun checkState(state: Int) {
        when (state) {
            PlayerState.STATE_PLAYING.state -> binding.btnPlayStop.setImageResource(R.drawable.ic_pause)
            PlayerState.STATE_PAUSED.state, PlayerState.STATE_DEFAULT.state -> binding.btnPlayStop.setImageResource(
                R.drawable.ic_play
            )

            PlayerState.STATE_PREPARED.state -> {
                binding.btnPlayStop.setImageResource(R.drawable.ic_play)
                binding.tViewDuration.setText(R.string.defaultDuration)
            }
        }
    }

    private fun listenState() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    checkState(state)
                    handler.postDelayed(
                        this,
                        PLAY_DEBOUNCE_DELAY
                    )
                }
            },
            PLAY_DEBOUNCE_DELAY
        )
    }

    companion object {
        const val PLAY_DEBOUNCE_DELAY = 300L
    }
}