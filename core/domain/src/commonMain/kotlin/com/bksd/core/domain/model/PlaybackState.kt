package com.bksd.core.domain.model

/**
 * Represents the current state of audio playback.
 * Lives in domain layer so both domain contracts and presentation can use it
 * without framework leakage.
 */
enum class PlaybackState {
    PLAYING,
    PAUSED,
    STOPPED
}
