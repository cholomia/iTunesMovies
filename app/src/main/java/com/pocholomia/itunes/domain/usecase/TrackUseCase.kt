package com.pocholomia.itunes.domain.usecase

import androidx.paging.PagingData
import com.pocholomia.itunes.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackUseCase {

    fun getTracks(): Flow<PagingData<Track>>

    fun getTrackDetail(trackId: Long): Flow<Track>

    fun getPreviousVisit(): Long

    fun setNewVisit(millis: Long)

}