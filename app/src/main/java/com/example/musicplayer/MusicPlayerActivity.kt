package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.models.FavoriteMusic
import com.google.firebase.database.FirebaseDatabase

class MusicPlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var musicTitleTextView: TextView
    private lateinit var playPauseButton: ImageButton
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var favoriteButton: ImageButton
    private lateinit var favoriteListButton: ImageButton
    private lateinit var database: FirebaseDatabase
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREF_VOLUME_LEVEL = "volume_level"
        const val PREF_FAVORITE_STATUS = "favorite_status"
        const val FAVORITE_STATUS_SELECTED = "selected"
        const val FAVORITE_STATUS_NOT_SELECTED = "not_selected"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        musicTitleTextView = findViewById(R.id.musicTitleTextView)
        playPauseButton = findViewById(R.id.playPauseButton)
        volumeSeekBar = findViewById(R.id.volumeSeekBar)
        favoriteButton = findViewById(R.id.favoriteButton)
        favoriteListButton = findViewById(R.id.favoritesListButton)

        database = FirebaseDatabase.getInstance()
        volumeSeekBar.progress = 50

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val defaultVolumeLevel = sharedPreferences.getInt(PREF_VOLUME_LEVEL, -1)
        if (defaultVolumeLevel == -1) {
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val initialVolumeLevel = maxVolume / 2
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initialVolumeLevel, AudioManager.FLAG_SHOW_UI)

            val editor = sharedPreferences.edit()
            editor.putInt(PREF_VOLUME_LEVEL, initialVolumeLevel)
            editor.apply()
        }

        val url = intent.getStringExtra("musicUrl")
        val title = intent.getStringExtra("musicTitle")

        musicTitleTextView.text = title

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(url)
            prepare()
            start()
        }

        playPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                playPauseButton.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_icon_play)
            } else {
                mediaPlayer.start()
                playPauseButton.setImageResource(com.google.android.exoplayer2.ui.R.drawable.exo_controls_pause)
            }
        }

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                val volumeLevel = (progress * maxVolume) / 100
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, AudioManager.FLAG_SHOW_UI)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val favoriteStatus = sharedPreferences.getString(PREF_FAVORITE_STATUS, FAVORITE_STATUS_NOT_SELECTED)
        if (favoriteStatus == FAVORITE_STATUS_SELECTED) {
            setFavoriteButtonSelectedState()
        } else {
            setFavoriteButtonNotSelectedState()
        }

        favoriteButton.setOnClickListener {
            val favoritesRef = database.reference.child("favoriler")
            val musicRef = favoritesRef.push()
            val music = FavoriteMusic(title!!, url!!)

            musicRef.setValue(music)

            if (favoriteStatus == FAVORITE_STATUS_NOT_SELECTED) {
                setFavoriteButtonSelectedState()
                sharedPreferences.edit().putString(PREF_FAVORITE_STATUS, FAVORITE_STATUS_SELECTED).apply()
            } else {
                setFavoriteButtonNotSelectedState()
                sharedPreferences.edit().putString(PREF_FAVORITE_STATUS, FAVORITE_STATUS_NOT_SELECTED).apply()
            }
        }

        favoriteListButton.setOnClickListener {
            val intent = Intent(this, FavoritesListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun setFavoriteButtonSelectedState() {
        favoriteButton.setImageResource(R.drawable.not_like_icon)
        favoriteButton.setColorFilter(Color.RED)
    }

    private fun setFavoriteButtonNotSelectedState() {
        favoriteButton.setImageResource(R.drawable.like_icon)
        favoriteButton.clearColorFilter()
    }
}