package com.example.audioplayer

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.Exception

class MainActivity : AppCompatActivity() {
    private var mediaPlayer = MediaPlayer()
    private val result = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        try {
            mediaPlayer = MediaPlayer.create(baseContext, it.data?.data)
            seekBar.max = mediaPlayer.duration
            mediaPlayer.start()
            changeSeekBar()
        } catch (e: Exception) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageViewOpen.setOnClickListener {
            val openIntent = Intent()
                .setType("audio/*")
                .setAction(Intent.ACTION_GET_CONTENT)
            result.launch(openIntent)
        }
        imageButtonPlay.setOnClickListener {
            try {
                mediaPlayer.start()
                changeSeekBar()
            } catch (e: Exception) {}
        }
        imageButtonPause.setOnClickListener {
            try {
                mediaPlayer.pause()
            } catch (e: Exception) {}
        }
        imageButtonBack.setOnClickListener {
            try {
                mediaPlayer.seekTo(mediaPlayer.currentPosition - 2000)
            } catch (e: Exception) {}
        }
        imageButtonForward.setOnClickListener {
            try {
                mediaPlayer.seekTo(mediaPlayer.currentPosition + 2000)
            } catch (e: Exception) {}
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    try {
                        mediaPlayer.seekTo(progress)
                    } catch (e: Exception) {}
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun changeSeekBar() {
        seekBar.progress = mediaPlayer.currentPosition
        if (mediaPlayer.isPlaying) {
            Handler(Looper.getMainLooper()).postDelayed(
                Thread { changeSeekBar() },
                1000
            )
        }
    }

    override fun onStop() {
        super.onStop()

        mediaPlayer.release()
    }
}