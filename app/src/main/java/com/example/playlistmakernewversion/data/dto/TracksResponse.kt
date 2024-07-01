package com.example.playlistmakernewversion.data.dto

import com.example.playlistmakernewversion.domain.model.Track

class TracksResponse(val resultCount: Long, val results: List<Track>) {
}