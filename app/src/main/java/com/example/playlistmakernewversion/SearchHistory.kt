package com.example.playlistmakernewversion

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

const val SEARCH_HISTORY_KEY = "TRACK_LIST_SEARCH_KEY"

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun addTrek(track: Track) {
        val trackList = getList().toMutableList()
        var indexTrack = -1
        var isReplay = false

        trackList.forEachIndexed { index, t ->
            if (t.trackId == track.trackId) {
                indexTrack = index
                isReplay = true
            }
        }

        if (isReplay) {
            trackList.removeAt(indexTrack)
            trackList.add(0, track)
        } else {
            if (trackList.size >= 10) {
                trackList.add(0, track)
                trackList.removeLast()
            } else {
                trackList.add(0, track)
            }
        }

        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(trackList))
            .apply()
    }

    fun getList(): Array<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return Gson().fromJson(json, Array<Track>::class.java) ?: emptyArray()
    }

    fun clear() {
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(mutableListOf<Track>()))
            .apply()
    }

}