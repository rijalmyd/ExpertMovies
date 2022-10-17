package com.rijaldev.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rijaldev.core.databinding.ItemMovieBinding
import com.rijaldev.core.databinding.ItemMovieGridBinding
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.ui.adapter.MovieAdapter.Companion.DIFF_CALLBACK
import com.rijaldev.core.utils.Extensions.showImageInto

class MovieGridAdapter(val data: (Movie, ImageView) -> Unit) : ListAdapter<Movie, MovieGridAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemMovieGridBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                ivMovie.showImageInto(itemView.context, movie.posterPath)
                itemView.setOnClickListener { data.invoke(movie, ivMovie) }
            }
        }
    }
}