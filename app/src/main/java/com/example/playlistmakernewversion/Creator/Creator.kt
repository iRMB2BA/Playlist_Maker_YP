package com.example.playlistmakernewversion.Creator

import android.content.Context
import com.example.playlistmakernewversion.player.data.repository.PlayerRepositoryImpl
import com.example.playlistmakernewversion.player.domain.api.PlayerInteractor
import com.example.playlistmakernewversion.player.domain.api.TrackStateListener
import com.example.playlistmakernewversion.player.domain.api.TrackTimeListener
import com.example.playlistmakernewversion.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmakernewversion.player.domain.repository.PlayerRepository
import com.example.playlistmakernewversion.settings.data.SharedPreferencesThemeSettings
import com.example.playlistmakernewversion.settings.data.repository.SettingRepositoryImpl
import com.example.playlistmakernewversion.settings.domain.api.SettingInteractor
import com.example.playlistmakernewversion.settings.domain.api.SettingRepository
import com.example.playlistmakernewversion.settings.domain.impl.SettingInteractorImpl

object Creator {


    private fun getPlayerRepository(trackTimeListener: TrackTimeListener, stateListener: TrackStateListener): PlayerRepository {
        return PlayerRepositoryImpl(trackTimeListener, stateListener)
    }


    fun providePlayerInteractor(trackTimeListener: TrackTimeListener, stateListener: TrackStateListener): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository(trackTimeListener, stateListener))
    }


//    private fun getTrackRepository(): TrackRepository {
//        return TrackRepositoryImpl(RetrofitNetworkClient())
//    }
//
//    fun provideTrackInteractor(): TrackInteractor {
//        return TrackInteraktorImpl(getTrackRepository())
//    }

    private fun getSettingRepository(context: Context): SettingRepository {
        return SettingRepositoryImpl(context, SharedPreferencesThemeSettings(context))
    }

    fun provideSettingInteractor(context: Context): SettingInteractor {
        return SettingInteractorImpl(getSettingRepository(context))
    }

//    private fun getHistoryRepository(context: Context) : SharedPreferensecHistory {
//        return SharedPreferencesHistoryImpl(context)
//    }
//
//    fun provideHistoryInteractor(context: Context) : TrackHistoryInteractor {
//        return TrackHistoryInteractorImpl(getHistoryRepository(context))
//    }


}