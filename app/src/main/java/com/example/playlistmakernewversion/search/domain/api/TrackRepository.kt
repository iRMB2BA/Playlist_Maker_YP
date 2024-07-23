package com.example.playlistmakernewversion.search.domain.api

import com.example.playlistmakernewversion.player.domain.models.Track
import com.example.playlistmakernewversion.utill.Resource

interface TrackRepository {

    fun code(): Int

    fun searchTrack(expression: String): Resource<List<Track>>

}