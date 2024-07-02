package com.example.playlistmakernewversion.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.creator.Creator
import com.example.playlistmakernewversion.databinding.ActivityPlayerBinding
import com.example.playlistmakernewversion.domain.OnPlayerStateChangeListener
import com.example.playlistmakernewversion.domain.model.Track
import com.example.playlistmakernewversion.ui.search.KEY_TRACK_INTENT
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private var playerState = "STATE_DEFAULT"
    private val interactor = Creator.providePlayerInteractor()
    private val trackTransfer = Creator.provideTrackTransfer()

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        binding.toolBar.setOnClickListener {
            finish()
        }

        track = trackTransfer.getTrack(intent.getStringExtra(KEY_TRACK_INTENT).toString())

        binding.btnPlayStop.setOnClickListener {
            playbackControl()
        }


        interactor.preparePlayer(track,
            object : OnPlayerStateChangeListener {
                override fun onChange(state: String) {
                    playerState = state
                }
            }
        )

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

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }

            STATE_COMPLETE -> {
                binding.btnPlayStop.setImageDrawable(
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.ic_play
                    )
                )
                playerState = STATE_PREPARED
                showTrackPlayedTime()
            }
        }
    }

    private fun showTrackPlayedTime() {
        when (playerState) {
            STATE_PLAYING -> {
                handler.postDelayed(
                    object : Runnable {
                        override fun run() {
                            binding.tViewDuration.text = interactor.getCurrentPosition()
                            if (playerState == STATE_PLAYING) {
                                handler.postDelayed(this, PLAY_DEBOUNCE_DELAY)
                            }
                        }
                    }, PLAY_DEBOUNCE_DELAY
                )
            }

            STATE_PAUSED -> {
                handler.removeCallbacksAndMessages(null)
            }

            STATE_PREPARED -> {
                handler.removeCallbacksAndMessages(null)
                binding.tViewDuration.setText(R.string.defaultDuration)
            }
        }
    }

    private fun startPlayer() {
        interactor.startPlayer()
        binding.btnPlayStop.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.ic_pause
            )
        )
        playerState = STATE_PLAYING
        showTrackPlayedTime()
    }

    private fun pausePlayer() {
        interactor.pausePlayer()
        binding.btnPlayStop.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.ic_play
            )
        )
        playerState = STATE_PAUSED
        showTrackPlayedTime()
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
        pausePlayer()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        interactor.release()
        super.onDestroy()
    }

    companion object {
        const val STATE_PREPARED = "STATE_PREPARED"
        const val STATE_PLAYING = "STATE_PLAYING"
        const val STATE_PAUSED = "STATE_PAUSED"
        const val STATE_COMPLETE = "STATE_COMPLETE"
        const val PLAY_DEBOUNCE_DELAY = 300L
    }
}