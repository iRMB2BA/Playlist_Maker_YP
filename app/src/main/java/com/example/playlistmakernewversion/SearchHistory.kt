package com.example.playlistmakernewversion

import android.content.SharedPreferences
import com.google.gson.Gson


class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun addTrek(track: Track) {
        val trackList = getList().toMutableList()
        trackList.add(track)
        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString("TRACK_LIST_SEARCH_KEY", json)
            .apply()
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
