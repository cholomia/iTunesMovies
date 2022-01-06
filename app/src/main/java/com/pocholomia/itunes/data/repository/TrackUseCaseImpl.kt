package com.pocholomia.itunes.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pocholomia.itunes.data.local.TrackDatabase
import com.pocholomia.itunes.domain.Constants
import com.pocholomia.itunes.domain.model.Track
import com.pocholomia.itunes.domain.usecase.TrackUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackUseCaseImpl @Inject constructor(
    private val trackDatabase: TrackDatabase,
    private val trackRemoteMediator: TrackRemoteMediator,
    private val sharedPreferences: SharedPreferences
) : TrackUseCase {

    @OptIn(ExperimentalPagingApi::class)
    override fun getTracks(): Flow<PagingData<Track>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            remoteMediator = trackRemoteMediator,
            pagingSourceFactory = { trackDatabase.trackDao().getTracks() }
        ).flow
    }

    override fun getTrackDetail(trackId: Long): Flow<Track> {
        return trackDatabase.trackDao().getTrack(trackId)
    }

    override fun getPreviousVisit(): Long {
        return sharedPreferences.getLong(Constants.PREVIOUS_VISIT, 0L)
    }

    override fun setNewVisit(millis: Long) {
        sharedPreferences.edit(true) { putLong(Constants.PREVIOUS_VISIT, millis) }
    }

}