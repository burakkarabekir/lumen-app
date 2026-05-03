package com.bksd.core.presentation

/**
 * Defines the visual mode for [AudioPlaybackStrip].
 *
 * [STANDARD] — Full-size playback with visualizer waveform and
 *              position / duration labels. Used on detail and create screens.
 *
 * [COMPACT]  — Minimal playback row with a smaller visualizer and
 *              duration label only. Used in list items.
 */
enum class AudioPlaybackMode {
    STANDARD,
    COMPACT
}
