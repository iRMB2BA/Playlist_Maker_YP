package com.example.playlistmakernewversion.player.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmakernewversion.R
import com.example.playlistmakernewversion.databinding.ActivityPlayerBinding
import com.example.playlistmakernewversion.player.domain.StatePlayer
import com.example.playlistmakernewversion.player.ui.view_model.MediaPlayerViewModel

const val EXTRA_TRACK_NAME = "track_name"
const val EXTRA_ARTIST_NAME = "artist_name"
const val EXTRA_TIME_MILLIS = "time_millis"
const val EXTRA_IMAGE = "track_Image"
const val EXTRA_COUNTRY = "track_country"
const val EXTRA_DATA = "track_data"
const val EXTRA_PRIMARY_NAME = "track_primary_name"
const val EXTRA_COLLECTION_NAME = "track_collection_name"
const val EXTRA_SONG = "track_song"


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var viewModel: MediaPlayerViewModel

    private var songUrl: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songUrl = intent.getStringExtra(EXTRA_SONG).toString()

        viewModel = ViewModelProvider(this)[MediaPlayerViewModel::class.java]

        viewModel.preparePlayer(songUrl)


        binding.btnPlayStop.setOnClickListener {
            viewModel.playStart()
        }

        viewModel.checkState.observe(this) {
            checkState(it)
        }

        binding.apply {
            tViewTrackTitle.text = intent.getStringExtra(EXTRA_TRACK_NAME)
            tViewArtistName.text = intent.getStringExtra(EXTRA_ARTIST_NAME)
            tViewTrackCountryValue.text = intent.getStringExtra(EXTRA_COUNTRY)

            val albumText = intent.getStringExtra(EXTRA_COLLECTION_NAME)
            if (albumText != null) {
                tViewTrackAlbumValue.text = albumText
            } else {
                tViewTrackAlbumValue.visibility = View.GONE
                tViewTrackAlbumText.visibility = View.GONE
            }

            tViewTrackGenreValue.text = intent.getStringExtra(EXTRA_PRIMARY_NAME)
        }

        binding.toolBar.setOnClickListener {
            finish()
        }


        val data = intent.getStringExtra(EXTRA_DATA).toString()
        viewModel.correctDataSong(data)
        viewModel.dataSong.observe(this) {
            binding.tViewTrackYearValue.text = it
        }


        val time = intent.getStringExtra(EXTRA_TIME_MILLIS)

        if (time != null) {
            viewModel.correctTimeSong(time)

            viewModel.timeSong.observe(this) {
                binding.tViewTrackDurationValue.text = it
            }
        }

        val urlImage = intent.getStringExtra(EXTRA_IMAGE)
        viewModel.getCoverArtwork(urlImage)

        viewModel.coverArtwork.observe(this) {
            var url = it

            val cornerSize = resources.getDimensionPixelSize(R.dimen.corners_image_track)
            Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(cornerSize))
                .into(binding.TrackCover)
        }

        viewModel.secondCounter.observe(this) { time ->
            binding.tViewDuration.text = time
        }


    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


    private fun checkState(state: StatePlayer) {
        when (state) {
            StatePlayer.STATE_PLAYING -> {
                binding.btnPlayStop.setImageResource(R.drawable.ic_pause)
            }

            StatePlayer.STATE_PAUSED, StatePlayer.STATE_DEFAULT -> {
                binding.btnPlayStop.setImageResource(R.drawable.ic_play)
            }

            StatePlayer.STATE_PREPARED -> {
                binding.btnPlayStop.setImageResource(R.drawable.ic_play)
                binding.tViewDuration.text = DEFAULT_TIME_TRACK
            }
        }
    }

    companion object {
        private const val DEFAULT_TIME_TRACK = "00:00"
    }
}