package com.rijaldev.expertmovies.ui.video

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.rijaldev.core.BuildConfig
import com.rijaldev.expertmovies.databinding.ActivityVideoBinding

class VideoActivity : YouTubeBaseActivity() {

    private val binding: ActivityVideoBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityVideoBinding.inflate(layoutInflater)
    }
    private var mPlayer: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.player.initialize(BuildConfig.YOUTUBE_API_KEY, initializeListener)
    }

    private val initializeListener = object : OnInitializedListener {
        override fun onInitializationSuccess(
            p0: YouTubePlayer.Provider?,
            player: YouTubePlayer?,
            wasRestored: Boolean,
        ) {
            if (player == null) return
            mPlayer = player
            val videoId = intent.getStringExtra(EXTRA_VIDEO)
            if (!wasRestored) {
                mPlayer?.loadVideo(videoId, 0)
                mPlayer?.play()
            }
        }

        override fun onInitializationFailure(
            p0: YouTubePlayer.Provider?,
            result: YouTubeInitializationResult?,
        ) {
            Toast.makeText(
                this@VideoActivity,
                result?.name,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStop() {
        super.onStop()
        mPlayer?.release()
        mPlayer = null
    }

    companion object {
        const val EXTRA_VIDEO = "extra_video"
    }
}