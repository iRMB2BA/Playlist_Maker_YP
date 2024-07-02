package com.example.playlistmakernewversion.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDTO(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val coverUrl: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl : String
)