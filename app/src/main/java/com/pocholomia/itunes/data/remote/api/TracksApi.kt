package com.pocholomia.itunes.data.remote.api

import com.pocholomia.itunes.data.remote.response.TracksApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TracksApi {

    @GET("search?term=star&country=au&media=movie&all")
    suspend fun getTracks(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): TracksApiResponse

}