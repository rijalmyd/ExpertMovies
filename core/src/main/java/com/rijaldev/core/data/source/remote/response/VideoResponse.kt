package com.rijaldev.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VideoResponse(

	@field:SerializedName("results")
	val results: List<VideoItem?>? = null
)