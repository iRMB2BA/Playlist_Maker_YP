package com.example.playlistmakernewversion.search.domain.api

import com.example.playlistmakernewversion.player.domain.models.Track

interface TrackInteractor {

    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: List<Track>?, errorMessage: String?)
    }

}