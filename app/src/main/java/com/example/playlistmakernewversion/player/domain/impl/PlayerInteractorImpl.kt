package com.example.playlistmakernewversion.player.domain.impl

import com.example.playlistmakernewversion.player.domain.api.PlayerInteractor
import com.example.playlistmakernewversion.player.domain.repository.PlayerRepository

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