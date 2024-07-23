package com.example.playlistmakernewversion.player.domain.api

import com.example.playlistmakernewversion.player.domain.StatePlayer

interface PlayerInteractor {

    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(trackUrl : String)
    fun releasePlayer()

    fun getState() : StatePlayer

}