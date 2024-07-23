package com.example.playlistmakernewversion.search.data.network

import com.example.playlistmakernewversion.search.data.NetworkClient
import com.example.playlistmakernewversion.search.data.dto.Response
import com.example.playlistmakernewversion.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val iTunesBaseURL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()






    override fun doRequest(dto: Any): Response {
        try {
            val iTunesService = retrofit.create(TrackApi::class.java)
            if (dto is TrackSearchRequest) {
                val resp = iTunesService.search(dto.expression).execute()
                val body = resp.body()?: Response()
                return body.apply{resultCode=resp.code()}
            }
            else {
                return Response().apply{resultCode=400}
            }
        } catch (e : Exception){
            return Response().apply{resultCode=400}
        }

    }
//
}