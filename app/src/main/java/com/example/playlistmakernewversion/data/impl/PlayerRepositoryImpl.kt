package com.example.playlistmakernewversion.data.impl

import android.media.MediaPlayer
import com.example.playlistmakernewversion.domain.OnPlayerStateChangeListener
import com.example.playlistmakernewversion.domain.model.Track
import com.example.playlistmakernewversion.domain.repository.PlayerRepository
import com.example.playlistmakernewversion.ui.player.PlayerActivity.Companion.STATE_COMPLETE
import com.example.playlistmakernewversion.ui.player.PlayerActivity.Companion.STATE_PREPARED
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl : PlayerRepository {

    private val mediaPlayer = MediaPlayer()
    private lateinit var listener: OnPlayerStateChangeListener

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun preparePlayer(
        track: Track,
        listener: OnPlayerStateChangeListener
    ) {
        this.listener = listener
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener.onChange(STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            listener.onChange(STATE_COMPLETE)
        }
    }

    override fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayer.currentPosition)
    }

    override fun release() {
        mediaPlayer.release()
    }


}