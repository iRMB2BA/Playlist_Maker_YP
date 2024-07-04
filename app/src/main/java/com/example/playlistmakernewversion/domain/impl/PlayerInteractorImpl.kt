package com.example.playlistmakernewversion.domain.impl

import com.example.playlistmakernewversion.domain.api.PlayerInteractor
import com.example.playlistmakernewversion.domain.models.Track
import com.example.playlistmakernewversion.domain.repository.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun playbackControl() {
        repository.playbackControl()
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun preparePlayer(trackUrl: String) {
        repository.preparePlayer(trackUrl)
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }


}