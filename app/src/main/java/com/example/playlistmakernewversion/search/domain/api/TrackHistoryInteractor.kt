package com.example.playlistmakernewversion.search.domain.api

import com.example.playlistmakernewversion.player.domain.models.Track

interface TrackHistoryInteractor {

    fun addHistoryTracks(tracksHistory: ArrayList<Track>)
    fun editHistoryList(tracksHistory: ArrayList<Track>)
    fun clearTrack(tracksHistory: ArrayList<Track>)
    fun addTrackInAdapter(track: Track)
}