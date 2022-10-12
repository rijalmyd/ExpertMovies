package com.rijaldev.expertmovies.ui.video

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.rijaldev.core.BuildConfig
import com.rijaldev.expertmovies.databinding.ActivityVideoBinding

class VideoActivity : YouTubeBaseActivity() {

    private lateinit var binding: ActivityVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        val videoId = intent.getStringExtra(EXTRA_VIDEO)
        binding.player.initialize(BuildConfig.YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean,
            ) {
                player?.loadVideo(videoId)
                player?.play()
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                result: YouTubeInitializationResult?,
            ) {
                Toast.makeText(this@VideoActivity, result?.name, Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val EXTRA_VIDEO = "extra_video"
    }
}