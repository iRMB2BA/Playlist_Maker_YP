package com.example.playlistmakernewversion.ui.player

enum class PlayerState(val state: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3),
}