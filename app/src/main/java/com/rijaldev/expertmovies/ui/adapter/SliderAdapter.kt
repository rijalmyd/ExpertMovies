package com.rijaldev.expertmovies.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.utils.DataMapper
import com.rijaldev.core.utils.Extensions.addChips
import com.rijaldev.core.utils.Extensions.showImageInto
import com.rijaldev.expertmovies.databinding.ItemImageSliderBinding
import com.rijaldev.expertmovies.ui.adapter.MovieAdapter.Companion.DIFF_CALLBACK

class SliderAdapter(val data: (Movie) -> Unit) : ListAdapter<Movie, SliderAdapter.SliderViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    inner class SliderViewHolder(val binding: ItemImageSliderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                ivSlider.showImageInto(itemView.context, movie.posterPath)
                cgGenres.removeAllViews()
                DataMapper.mapGenreIdToGenre(movie.genreIds)
                    .filterNotNull()
                    .take(3)
                    .forEach { cgGenres.addChips(itemView.context, it) }
            }
        }
    }
}