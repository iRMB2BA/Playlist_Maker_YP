package com.example.playlistmakernewversion.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmakernewversion.Creator.Creator
import com.example.playlistmakernewversion.player.domain.StatePlayer
import com.example.playlistmakernewversion.player.domain.api.TrackStateListener
import com.example.playlistmakernewversion.player.domain.api.TrackTimeListener
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerViewModel() : ViewModel(), TrackTimeListener, TrackStateListener {
    private val playerInteractor = Creator.providePlayerInteractor(this, this)

    private val _secondCounter = MutableLiveData<String>()
    val secondCounter: LiveData<String> = _secondCounter

    private val _checkState = MutableLiveData<StatePlayer>()
    val checkState: LiveData<StatePlayer> = _checkState

    private val _timeSong = MutableLiveData<String>()
    val timeSong: LiveData<String> = _timeSong

    private val _dataSong = MutableLiveData<String>()
    val dataSong: LiveData<String> = _dataSong

    private val _coverArtwork = MutableLiveData<String>()
    val coverArtwork: LiveData<String> = _coverArtwork


    fun getCoverArtwork(urlImage: String?) {
        _coverArtwork.value = urlImage?.replaceAfterLast('/', "512x512bb.jpg")
    }

    fun correctDataSong(data: String) {
        _dataSong.value = data.substring(0, 4)
    }

    fun correctTimeSong(time: String?) {
        _timeSong.value = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time?.toInt())
    }


    fun preparePlayer(urlTrack: String) {
        playerInteractor.preparePlayer(urlTrack)
    }

    fun playStart() {
        playerInteractor.playbackControl()

    }

    fun onPause() {
        playerInteractor.pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
    }

    override fun onTimeChanged(time: String) {
        _secondCounter.value = time
    }

    override fun getState(state: StatePlayer) {
        _checkState.value = state
    }


    companion object {
        private const val REFRESH_STATE = 1L
    }


}