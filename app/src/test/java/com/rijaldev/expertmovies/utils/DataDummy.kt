package com.rijaldev.expertmovies.utils

import com.rijaldev.core.domain.model.Movie
import com.rijaldev.core.domain.model.Video

object DataDummy {
    fun getDummyMovies(): List<Movie> {
        val listMovie = ArrayList<Movie>()
        (1..10).forEach { id ->
            val movie = Movie(
                id,
                "tes",
                "https://tes.jpg",
                "id",
                "2022",
                9.0,
                7.9,
                "title",
                listOf(2, 4),
                80,
                "https://tes.jpg",
                "tes",
                false
            )
            listMovie.add(movie)
        }
        return listMovie
    }

    fun getDummyVideos(): List<Video> {
        val listVideo = ArrayList<Video>()
        (1..10).forEach { id ->
            val video = Video(
                "name",
                true,
                id.toString(),
                "key"
            )
            listVideo.add(video)
        }
        return listVideo
    }
}