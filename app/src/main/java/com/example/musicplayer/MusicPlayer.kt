package com.example.musicplayer

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class MusicPlayer(private val context: Context) {
    private var player: SimpleExoPlayer? = null

    fun playMusic(url: String) {
        releasePlayer()

        val dataSourceFactory = DefaultDataSourceFactory(context, "MusicPlayer")
        val mediaItem = MediaItem.fromUri(Uri.parse(url))
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        player = SimpleExoPlayer.Builder(context)
            .build()

        player?.setMediaSource(mediaSource)
        player?.prepare()
        player?.play()
    }

    fun pauseMusic() {
        player?.pause()
    }

    fun stopMusic() {
        player?.stop()
        releasePlayer()
    }

    internal fun releasePlayer() {
        player?.release()
        player = null
    }
}