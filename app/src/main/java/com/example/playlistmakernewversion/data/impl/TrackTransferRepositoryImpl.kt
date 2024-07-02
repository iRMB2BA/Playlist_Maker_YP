package com.example.playlistmakernewversion.data.impl

import com.example.playlistmakernewversion.domain.model.Track
import com.example.playlistmakernewversion.ui.repository.TrackTransferRepository
import com.google.gson.Gson

class TrackTransferRepositoryImpl : TrackTransferRepository {
    override fun getTrack(trackInfo: String): Track {
        return Gson().fromJson(trackInfo, Track::class.java)
    }

    override fun sendTrack(track: Track): String {
        return Gson().toJson(track)
    }
}
