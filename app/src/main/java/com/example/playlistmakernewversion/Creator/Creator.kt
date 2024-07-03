package com.example.playlistmakernewversion.Creator

import com.example.playlistmakernewversion.data.repository.PlayerRepositoryImpl
import com.example.playlistmakernewversion.domain.api.PlayerInteractor
import com.example.playlistmakernewversion.domain.api.TrackStateListener
import com.example.playlistmakernewversion.domain.api.TrackTimeListener
import com.example.playlistmakernewversion.domain.impl.PlayerInteractorImpl
import com.example.playlistmakernewversion.domain.repository.PlayerRepository

object Creator {


    private fun getPlayerRepository(
        trackTimeListener: TrackTimeListener,
        stateListener: TrackStateListener
    ): PlayerRepository {
        return PlayerRepositoryImpl(trackTimeListener, stateListener)
    }


    fun providePlayerInteractor(
        trackTimeListener: TrackTimeListener,
        stateListener: TrackStateListener
    ): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(trackTimeListener, stateListener))
    }


}