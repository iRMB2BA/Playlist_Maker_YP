package com.example.playlistmakernewversion.search.ui.adapter

import com.example.playlistmakernewversion.player.domain.models.Track

fun interface TrackClickListener {
    fun onTrackClick(track: Track)
}