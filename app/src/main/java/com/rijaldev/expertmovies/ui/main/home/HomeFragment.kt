package com.rijaldev.expertmovies.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.rijaldev.core.data.repository.Resource
import com.rijaldev.core.domain.model.MovieType
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.expertmovies.databinding.FragmentHomeBinding
import com.rijaldev.core.ui.adapter.MovieAdapter
import com.rijaldev.core.ui.adapter.SliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showImageSlider()
        setUpTransformer()
        showNowPlayingMovies()
        showPopularMovies()
        showUpcomingMovies()
        showTopRatedMovies()
    }

    private fun showImageSlider() {
        val adapter = SliderAdapter { movie, iv ->
            moveToDetail(movie, iv)
        }
        viewModel.trendingMovies.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    Toast.makeText(requireActivity(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding?.contentHome?.vpImage?.apply {
            this.adapter = adapter
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    swipe()
                }
            }
        }
        binding?.contentHome?.apply {
            textNowPlaying.setOnClickListener { navigateMovies(MovieType.NOW_PLAYING, "Now Playing") }
            textPopular.setOnClickListener { navigateMovies(MovieType.POPULAR, "Popular") }
            textTopRated.setOnClickListener { navigateMovies(MovieType.TOP_RATED, "Top Rated") }
            textUpcoming.setOnClickListener { navigateMovies(MovieType.UPCOMING, "Upcoming") }
        }
    }

    private tailrec suspend fun ViewPager2.swipe() {
        delay(3000L)
        val numberItems = adapter?.itemCount ?: 0
        val lastIndex = if (numberItems > 0) numberItems - 1 else 0
        val nextItem = if (currentItem == lastIndex) 0 else currentItem + 1
        setCurrentItem(nextItem, true)
        swipe()
    }

    private fun navigateMovies(movieType: MovieType, title: String) {
        val data = HomeFragmentDirections.actionHomeFragmentToMoviesActivity(movieType, title)
        findNavController().navigate(data)
    }

    private fun showNowPlayingMovies() {
        val adapter = MovieAdapter { movie, iv ->
            moveToDetail(movie, iv)
        }
        binding?.contentHome?.apply {
            rvNowPlaying.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            rvNowPlaying.setHasFixedSize(true)
            rvNowPlaying.adapter = adapter
        }
        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun showPopularMovies() {
        val adapter = MovieAdapter { movie, iv ->
            moveToDetail(movie, iv)
        }
        binding?.contentHome?.apply {
            rvPopular.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            rvPopular.setHasFixedSize(true)
            rvPopular.adapter = adapter
        }
        viewModel.popularMovies.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun showUpcomingMovies() {
        val adapter = MovieAdapter { movie, iv ->
            moveToDetail(movie, iv)
        }
        binding?.contentHome?.apply {
            rvUpcoming.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            rvUpcoming.setHasFixedSize(true)
            rvUpcoming.adapter = adapter
        }
        viewModel.upComingMovies.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun showTopRatedMovies() {
        val adapter = MovieAdapter { movie, iv ->
            moveToDetail(movie, iv)
        }
        binding?.contentHome?.apply {
            rvTopRated.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            rvTopRated.setHasFixedSize(true)
            rvTopRated.adapter = adapter
        }
        viewModel.topRatedMovies.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {}
            }
        }
    }

    private fun moveToDetail(movie: Movie, iv: ImageView) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            Pair(iv, "iv_movie")
        )
        val extras = ActivityNavigatorExtras(options)
        val data = HomeFragmentDirections.actionHomeFragmentToDetailActivity(movie)
        findNavController().navigate(data, extras)
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        binding?.contentHome?.vpImage?.setPageTransformer(transformer)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}