package com.rijaldev.expertmovies.ui.main.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rijaldev.expertmovies.databinding.FragmentMoviesBinding
import com.rijaldev.expertmovies.ui.adapter.LoadingStateAdapter
import com.rijaldev.expertmovies.ui.adapter.MoviePagingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding
    private val viewModel: MoviesViewModel by viewModels()
    private val navArgs: MoviesFragmentArgs by navArgs()
    private lateinit var movieAdapter: MoviePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpView()
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = navArgs.title
    }

    private fun setUpView() {
        movieAdapter = MoviePagingAdapter { movie, iv ->
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                Pair(iv, "iv_movie")
            )
            val extras = ActivityNavigatorExtras(options)
            val data = MoviesFragmentDirections.actionMoviesFragmentToDetailActivity(movie)
            findNavController().navigate(data, extras)
        }
        val loadStateAdapter = LoadingStateAdapter {
            movieAdapter.retry()
        }
        binding?.rvMovies?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapter.withLoadStateFooter(loadStateAdapter)
            setHasFixedSize(true)
        }
        viewModel.getMoviesPaging(navArgs.movieType).observe(viewLifecycleOwner) {
            movieAdapter.submitData(lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}