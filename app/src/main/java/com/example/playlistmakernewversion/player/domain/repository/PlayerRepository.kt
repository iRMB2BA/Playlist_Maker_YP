package com.example.playlistmakernewversion.player.domain.repository

import com.example.playlistmakernewversion.player.domain.PlayerState

interface PlayerRepository {



    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(trackUrl: String)
    fun releasePlayer()
    fun updateTime(time : String)
    fun getTime() : String

    fun getState() : PlayerState
}