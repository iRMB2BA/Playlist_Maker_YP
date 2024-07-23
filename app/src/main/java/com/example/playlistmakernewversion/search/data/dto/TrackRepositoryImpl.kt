package com.example.playlistmakernewversion.search.data.dto

import com.example.playlistmakernewversion.player.domain.models.Track
import com.example.playlistmakernewversion.search.data.NetworkClient
import com.example.playlistmakernewversion.search.data.SearchStatus
import com.example.playlistmakernewversion.search.domain.api.TrackRepository
import com.example.playlistmakernewversion.utill.Resource

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    var codeResult = 0

    override fun code(): Int {
        return codeResult
    }

    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> Resource.Error(SearchStatus.NO_INTERNET.nameStatus)
            200 -> {
                Resource.Success((response as TrackSearchResponse).results.map {
                    Track(
                        it.trackId,
                        it.artworkUrl100,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.collectionName,
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            else -> Resource.Error(SearchStatus.NO_INTERNET.nameStatus)
        }

    }
}