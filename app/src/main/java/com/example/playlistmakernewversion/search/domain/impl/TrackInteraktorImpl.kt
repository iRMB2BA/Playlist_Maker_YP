package com.example.playlistmakernewversion.search.domain.impl

import com.example.playlistmakernewversion.search.domain.api.TrackInteractor
import com.example.playlistmakernewversion.search.domain.api.TrackRepository
import com.example.playlistmakernewversion.utill.Resource
import java.util.concurrent.Executors

class TrackInteraktorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTrack(expression)) {
                is Resource.Success -> consumer.consume(resource.data, null)
                is Resource.Error -> consumer.consume(null, resource.message)
            }
        }
    }
}