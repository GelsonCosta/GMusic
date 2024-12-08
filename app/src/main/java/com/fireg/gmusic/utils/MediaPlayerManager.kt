package com.fireg.gmusic.utils

import android.media.MediaPlayer

object MediaPlayerManager {
    private var currentMediaPlayer: MediaPlayer? = null

    fun play(mediaPlayer: MediaPlayer) {

        currentMediaPlayer?.takeIf { it.isPlaying }?.stop()
        currentMediaPlayer?.reset()

        currentMediaPlayer = mediaPlayer
        mediaPlayer.start()
    }

    fun stopCurrent() {
        currentMediaPlayer?.stop()
        currentMediaPlayer?.reset()
        currentMediaPlayer = null
    }
}
