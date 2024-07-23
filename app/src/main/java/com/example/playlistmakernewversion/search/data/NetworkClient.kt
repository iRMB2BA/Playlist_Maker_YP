package com.example.playlistmakernewversion.search.data

import com.example.playlistmakernewversion.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}