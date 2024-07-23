package com.example.playlistmakernewversion.search.domain.impl

import com.example.playlistmakernewversion.player.domain.models.Track
import com.example.playlistmakernewversion.search.domain.api.SharedPreferensecHistory
import com.example.playlistmakernewversion.search.domain.api.TrackHistoryInteractor

class TrackHistoryInteractorImpl(private val sharedHistory: SharedPreferensecHistory) :
    TrackHistoryInteractor {

    override fun addHistoryTracks(tracksHistory: ArrayList<Track>) {
        sharedHistory.addHistoryTracks(tracksHistory)
    }

    override fun editHistoryList(tracksHistory: ArrayList<Track>) {
        sharedHistory.editHistoryList(tracksHistory)
    }

    override fun clearTrack(tracksHistory: ArrayList<Track>) {
        sharedHistory.clearTrack(tracksHistory)
    }

    override fun addTrackInAdapter(track: Track) {
        sharedHistory.addTrackInAdapter(track)
    }
}