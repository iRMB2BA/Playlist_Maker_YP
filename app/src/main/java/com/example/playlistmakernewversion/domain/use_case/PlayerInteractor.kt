package com.example.playlistmakernewversion.domain.use_case

import com.example.playlistmakernewversion.domain.OnPlayerStateChangeListener
import com.example.playlistmakernewversion.domain.model.Track

interface PlayerInteractor {
    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(track: Track, listener: OnPlayerStateChangeListener)

    fun getCurrentPosition(): String

    fun release()
}