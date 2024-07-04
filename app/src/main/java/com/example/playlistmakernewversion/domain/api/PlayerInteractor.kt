package com.example.playlistmakernewversion.domain.api

import com.example.playlistmakernewversion.domain.models.Track

interface PlayerInteractor {

    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(trackUrl : String)
    fun releasePlayer()

}