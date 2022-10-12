package com.rijaldev.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rijaldev.expertmovies.di.SearchModuleDependencies
import com.rijaldev.expertmovies.ui.adapter.LoadingStateAdapter
import com.rijaldev.expertmovies.ui.adapter.MoviePagingAdapter
import com.rijaldev.search.databinding.FragmentSearchBinding
import com.rijaldev.search.di.DaggerSearchComponent
import com.rijaldev.search.viewmodel.ViewModelFactory
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var factory: ViewModelFactory
    private val viewModel: SearchViewModel by viewModels { factory }

    override fun onAttach(context: Context) {
        DaggerSearchComponent.builder()
            .context(requireActivity())
            .appDependencies(
                EntryPointAccessors.fromApplication(
                    requireActivity().applicationContext,
                    SearchModuleDependencies::class.java
                )
            ).build().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.query.value = it
                }
                binding?.searchView?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.query.value = it
                }
                return true
            }
        })
        val movieAdapter = MoviePagingAdapter {movie, iv ->
            viewModel.insertMovie(movie)
            binding?.searchView?.clearFocus()
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                Pair(iv, "iv_movie")
            )
            val extras = ActivityNavigatorExtras(options)
            val data = SearchFragmentDirections.actionSearchFragmentToDetailActivity(movie)
            findNavController().navigate(data, extras)
        }
        binding?.rvSearch?.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = movieAdapter.withLoadStateFooter(
                LoadingStateAdapter {
                    movieAdapter.retry()
                }
            )
            setHasFixedSize(true)
        }
        isMoviesEmpty(movieAdapter.itemCount < 1)
        lifecycleScope.launchWhenCreated {
            movieAdapter.loadStateFlow.collect {
                isMoviesEmpty(movieAdapter.itemCount < 1)
            }
        }
        viewModel.resultData.observe(viewLifecycleOwner) {
            movieAdapter.submitData(lifecycle, it)
        }
    }

    private fun isMoviesEmpty(isEmpty: Boolean) {
        binding?.apply {
            ivSearchEmpty.isVisible = isEmpty
            tvSearchEmpty.isVisible = isEmpty
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}