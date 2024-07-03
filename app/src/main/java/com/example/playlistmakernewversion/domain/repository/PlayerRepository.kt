package com.example.playlistmakernewversion.domain.repository

import com.example.playlistmakernewversion.domain.models.Track

interface PlayerRepository {



    fun playbackControl()
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(trackUrl: String)
    fun releasePlayer()
    fun updateTime(time : String)
    fun getTime() : String

}