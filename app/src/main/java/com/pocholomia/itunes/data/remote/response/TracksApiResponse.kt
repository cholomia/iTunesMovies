package com.pocholomia.itunes.data.remote.response

import com.pocholomia.itunes.domain.model.Track
import com.squareup.moshi.Json

data class TracksApiResponse(
    @field:Json(name = "resultCount") val resultCount: Int,
    @field:Json(name = "results") val results: List<Track>
)