package com.example.playlistmakernewversion.player.domain.api

import com.example.playlistmakernewversion.player.domain.StatePlayer

interface TrackStateListener {

    fun getState(state : StatePlayer)

}