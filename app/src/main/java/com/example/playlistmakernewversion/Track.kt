package com.example.playlistmakernewversion

data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)
