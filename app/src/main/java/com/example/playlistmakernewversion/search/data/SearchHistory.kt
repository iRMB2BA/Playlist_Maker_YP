package com.example.playlistmakernewversion.search.data

import android.content.SharedPreferences
import com.example.playlistmakernewversion.player.domain.models.Track
import com.example.playlistmakernewversion.search.ui.adapter.HistoryAdapter
import com.google.gson.Gson

class SearchHistory(private val sharedPref: SharedPreferences) {
    lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    private val historyAdapter = HistoryAdapter {}

    companion object {
        private const val HISTORY_TRACK_MAX = 10
    }

    fun addTrackHistory(tracksHistory: ArrayList<Track>) {
        historyAdapter.trackListHistory = tracksHistory
        tracksHistory.addAll(read(sharedPref))
        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefHistory, key ->
            if (key == KEY_HISTORY) {
                val track = sharedPrefHistory?.getString(key, null)
                if (track != null) {
                    repeatingTrackCheck(track, tracksHistory)
                    if (historyAdapter.trackListHistory.size >= HISTORY_TRACK_MAX) {
                        tracksHistory.removeAt(historyAdapter.trackListHistory.size - 1)
                    }
                    historyAdapter.trackListHistory.add(0, createTrackFromJson(track))
                    historyAdapter.notifyItemInserted(0)
                }
            }
        }
    }

    private fun createTrackFromJson(json: String?): Track {
        return Gson().fromJson(json, Track::class.java)
    }


    private fun read(sharedPreferences: SharedPreferences): Array<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY_ALL, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }


    private fun repeatingTrackCheck(track: String, tracksHistory: ArrayList<Track>) {
        val iterator = tracksHistory.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item.trackId == createTrackFromJson(track).trackId) {
                iterator.remove()
            }
        }
    }

}