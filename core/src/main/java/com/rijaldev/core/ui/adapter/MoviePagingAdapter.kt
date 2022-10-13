package com.rijaldev.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rijaldev.core.databinding.ItemMovieLargeBinding
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.utils.Extensions.showImageInto
import com.rijaldev.core.utils.Extensions.toAnotherDate
import com.rijaldev.core.ui.adapter.MovieAdapter.Companion.DIFF_CALLBACK

class MoviePagingAdapter(
    private val data: (Movie, ImageView) -> Unit
) : PagingDataAdapter<Movie, MoviePagingAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieLargeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let { holder.bind(it) }
    }

    inner class MovieViewHolder(private val binding: ItemMovieLargeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                ivMovie.showImageInto(itemView.context, movie.posterPath)
                tvTitle.text = movie.title
                tvDate.text = movie.releaseDate?.toAnotherDate()
                itemView.setOnClickListener {
                    data.invoke(movie, ivMovie)
                }
            }
        }
    }
}