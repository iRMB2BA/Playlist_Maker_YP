package com.example.playlistmakernewversion

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.playlistmakernewversion.databinding.ActivityPlayerBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        const val PLAY_DEBOUNCE_DELAY = 300L

    }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private var playerState = STATE_DEFAULT
    private lateinit var binding: ActivityPlayerBinding
    private val gson = Gson()
    private lateinit var handler: Handler
    private var mediaPlayer = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val track = gson.fromJson(intent.getStringExtra(KEY_TRACK_INTENT), Track::class.java)
        handler = Handler(Looper.getMainLooper())
        preparePlayer(track)


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

        binding.btnPlayStop.setOnClickListener {
            playbackControl()
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
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
        mediaPlayer.pause()
        binding.btnPlayStop.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.ic_play
            )
        )
        playerState = STATE_PAUSED
        showTrackPlayedTime()
    }

    private fun showTrackPlayedTime() {
        when (playerState) {
            STATE_PLAYING -> {
                handler.postDelayed(
                    object : Runnable {
                        override fun run() {
                            binding.tViewDuration.text = dateFormat.format(mediaPlayer.currentPosition)
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
                binding.tViewDuration.text = getString(R.string.defaultDuration)
            }
        }
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.btnPlayStop.isClickable = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
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
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayer.release()
    }
}