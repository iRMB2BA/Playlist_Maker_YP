package com.example.playlistmakernewversion

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson


class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun addTrek(track: Track): Int {
        var trackList = getList().toMutableList()
        var indexTrack = -1
        var change = false

        trackList.forEachIndexed { index, t ->
            if (t.trackId == track.trackId) {
                indexTrack = index
                change = true
                Log.i("bag", indexTrack.toString())
            }
        }

        if (change) {
            val array = mutableListOf<Track>()
            array.add(track)
            trackList.removeAt(indexTrack)
            array.addAll(trackList)
            trackList = array
        } else {
            if (trackList.size > 10) {
                trackList.removeLast()
                val array = mutableListOf<Track>()
                array.add(track)
                array.addAll(trackList)
                trackList = array
            } else {
                trackList.add(track)
            }
        }

        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString("TRACK_LIST_SEARCH_KEY", json)
            .apply()

        return indexTrack
    }

    fun getList(): Array<Track> {
        val json = sharedPreferences.getString("TRACK_LIST_SEARCH_KEY", null)
        return if (json != null) {
            Gson().fromJson(json, Array<Track>::class.java)
        } else {
            emptyArray()
        }
    }

    fun clear() {
        val trackList = getList().toMutableList()
        trackList.clear()
        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString("TRACK_LIST_SEARCH_KEY", json)
            .apply()
    }

}
