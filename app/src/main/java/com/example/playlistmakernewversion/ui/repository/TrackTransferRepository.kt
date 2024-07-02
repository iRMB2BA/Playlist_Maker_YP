package com.example.playlistmakernewversion.ui.repository

import com.example.playlistmakernewversion.domain.model.Track

interface TrackTransferRepository {
    fun getTrack(trackInfo: String): Track
    fun sendTrack(track: Track): String
}