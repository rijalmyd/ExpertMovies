package com.rijaldev.expertmovies.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.navArgs
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.rijaldev.core.data.repository.Resource
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.domain.model.Video
import com.rijaldev.core.utils.DataMapper
import com.rijaldev.core.utils.Extensions.showImageInto
import com.rijaldev.core.utils.Extensions.showThumbnailInto
import com.rijaldev.core.utils.Extensions.toAnotherDate
import com.rijaldev.expertmovies.R
import com.rijaldev.expertmovies.databinding.ActivityDetailBinding
import com.rijaldev.expertmovies.ui.video.VideoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val navArgs: DetailActivityArgs by navArgs()
    private var menu: Menu? = null
    private var statusFavorite: Boolean = false

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpView()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    private fun setUpView() {
        val movie = navArgs.movie
        populateDetail(movie)
        viewModel.isFavoriteMovie(movie.id).observe(this, favoriteObserver)
    }

    private fun populateDetail(data: Movie) {
        viewModel.getVideos(data.id).observe(this, videoObserver)
        binding.content.apply {
            ivBackdrop.showImageInto(this@DetailActivity, data.backdropPath)
            ivPoster.showImageInto(this@DetailActivity, data.posterPath)
            tvTitle.text = data.title
            tvOverview.text = data.overview
            tvDate.text = data.releaseDate?.toAnotherDate()
            tvLanguage.text = data.originalLanguage
            tvRating.text = data.voteAverage.toString()
            DataMapper.mapGenreIdToGenre(data.genreIds)
                .filterNotNull()
                .take(3)
                .forEach { tvGenres.append(" \u2022 $it") }
        }
    }

    private val videoObserver = Observer<Resource<List<Video>?>> { resource ->
        when (resource) {
            is Resource.Loading -> {}
            is Resource.Success -> {
                val result = resource.data
                result?.first()?.key?.setThumbnail()
            }
            is Resource.Error -> {
                binding.content.thumbnailView.isVisible = false
            }
        }
    }

    private fun String?.setThumbnail() {
        this?.let { movieId ->
            binding.content.thumbnailView.apply {
                showThumbnailInto(this@DetailActivity, movieId)
                setOnClickListener {
                    moveToVideoActivity(movieId)
                }
            }
        } ?: run {
            binding.content.thumbnailView.isVisible = false
        }
    }

    private fun installVideoModule(data: String) {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleVideo = "video"
        if (splitInstallManager.installedModules.contains(moduleVideo)) {
            moveToVideoActivity(data)
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleVideo)
                .build()

            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    moveToVideoActivity(data)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error installing ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun moveToVideoActivity(data: String) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra("extra_video", data)
        startActivity(intent)
    }

    private val favoriteObserver = Observer<Boolean> { isFavorite ->
        this.statusFavorite = isFavorite
        setFavoriteStatus(isFavorite)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        viewModel.isFavoriteMovie(navArgs.movie.id).observe(this, favoriteObserver)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.btn_favorite -> {
                statusFavorite = !statusFavorite
                val movie = navArgs.movie
                viewModel.update(movie.id, statusFavorite)
                setFavoriteStatus(statusFavorite)
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun setFavoriteStatus(statusFavorite: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.btn_favorite)
        if (statusFavorite) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_heart_fill)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_heart_white)
        }
    }
}   