package com.example.playlistmakernewversion.domain.repository

import com.example.playlistmakernewversion.domain.OnPlayerStateChangeListener
import com.example.playlistmakernewversion.domain.model.Track

interface PlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(track: Track, listener: OnPlayerStateChangeListener)
    fun getCurrentPosition (): String
    fun release()
}