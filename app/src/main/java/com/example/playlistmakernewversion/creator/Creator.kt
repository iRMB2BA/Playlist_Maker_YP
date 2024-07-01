package com.example.playlistmakernewversion.creator

import com.example.playlistmakernewversion.data.impl.PlayerRepositoryImpl
import com.example.playlistmakernewversion.data.impl.TrackTransferRepositoryImpl
import com.example.playlistmakernewversion.domain.impl.PlayerInteractorImpl
import com.example.playlistmakernewversion.domain.repository.PlayerRepository
import com.example.playlistmakernewversion.domain.use_case.PlayerInteractor
import com.example.playlistmakernewversion.ui.repository.TrackTransferRepository

object Creator {

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideTrackTransfer(): TrackTransferRepository {
        return TrackTransferRepositoryImpl()
    }
}