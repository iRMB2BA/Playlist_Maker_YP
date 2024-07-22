package com.example.playlistmakernewversion.player.domain

enum class PlayerState(val state: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3),
}