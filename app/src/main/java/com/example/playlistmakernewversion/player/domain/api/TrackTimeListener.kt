package com.example.playlistmakernewversion.player.domain.api

import com.example.playlistmakernewversion.player.domain.StatePlayer
import com.example.playlistmakernewversion.player.domain.repository.PlayerRepository

interface TrackTimeListener {

    fun onTimeChanged(time : String)

}


