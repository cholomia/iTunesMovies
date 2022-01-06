package com.pocholomia.itunes.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.pocholomia.itunes.data.local.TrackDatabase
import com.pocholomia.itunes.data.remote.api.TracksApi
import com.pocholomia.itunes.domain.Constants
import com.pocholomia.itunes.domain.model.RemoteKeys
import com.pocholomia.itunes.domain.model.Track
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TrackRemoteMediator @Inject constructor(
    private val tracksApi: TracksApi,
    private val trackDatabase: TrackDatabase,
    private val sharedPreferences: SharedPreferences
) : RemoteMediator<Int, Track>() {

    /**
     * skip automatic initial refresh if last refresh is within the STALE_TIMEOUT
     */
    override suspend fun initialize(): InitializeAction {
        val lastRefresh = sharedPreferences.getLong(Constants.LAST_REFRESH, 0L)
        return if ((System.currentTimeMillis() - lastRefresh) > Constants.STALE_TIMEOUT) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Track>): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                getRemoteKeyClosestToCurrentPosition(state)?.nextKey?.minus(1)
                    ?: Constants.STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }
        try {
            val offset = (page - 1) * Constants.ITEMS_PER_PAGE
            val trackApiResponse = tracksApi.getTracks(limit = Constants.ITEMS_PER_PAGE, offset)
            sharedPreferences.edit(true) { putLong(Constants.LAST_REFRESH, System.currentTimeMillis()) }
            val tracks = trackApiResponse.results
            val endOfPaginationReached = tracks.isEmpty()
            trackDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // clear all tables in the db
                    trackDatabase.remoteKeysDao().deleteAll()
                    trackDatabase.trackDao().deleteAll()
                }
                val prevKey = if (page == Constants.STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = tracks.map {
                    RemoteKeys(
                        trackId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                trackDatabase.remoteKeysDao().insertAll(keys)
                trackDatabase.trackDao().insertAll(tracks.mapIndexed { index, track ->
                    track.copy(sortOrder = (offset + index).toLong())
                })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Throwable) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Track>): RemoteKeys? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                trackDatabase.remoteKeysDao().remoteKeysTrackId(id)
            }
        }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Track>): RemoteKeys? =
        state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data
            ?.firstOrNull()
            ?.let { track ->
                trackDatabase.remoteKeysDao().remoteKeysTrackId(track.id)
            }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Track>): RemoteKeys? =
        state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data
            ?.lastOrNull()
            ?.let { track ->
                trackDatabase.remoteKeysDao().remoteKeysTrackId(track.id)
            }

}