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
import com.rijaldev.expertmovies.ui.adapter.MovieAdapter
import com.rijaldev.expertmovies.ui.adapter.SliderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var handler: Handler

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
        handler = Handler(Looper.myLooper() as Looper)
        val adapter = SliderAdapter {

        }
        viewModel.trendingMovies.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {

                }
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
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable, 3000L)
                }
            })
        }
        binding?.contentHome?.apply {
            textNowPlaying.setOnClickListener { navigateMovies(MovieType.NOW_PLAYING, "Now Playing") }
            textPopular.setOnClickListener { navigateMovies(MovieType.POPULAR, "Popular") }
            textTopRated.setOnClickListener { navigateMovies(MovieType.TOP_RATED, "Top Rated") }
            textUpcoming.setOnClickListener { navigateMovies(MovieType.UPCOMING, "Upcoming") }
        }
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
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {

                }
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
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {

                }
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
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {

                }
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
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    adapter.submitList(resource.data)
                }
                is Resource.Error -> {

                }
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

    private val runnable = Runnable {
        binding?.contentHome?.vpImage?.apply {
            currentItem += 1
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 3000L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}