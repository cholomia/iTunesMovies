package com.pocholomia.itunes.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pocholomia.itunes.domain.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Query("SELECT * FROM tracks ORDER BY sortOrder ASC")
    fun getTracks(): PagingSource<Int, Track>

    @Query("SELECT * FROM tracks WHERE id=:trackId")
    fun getTrack(trackId: Long): Flow<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tracks: List<Track>)

    @Query("DELETE FROM tracks")
    suspend fun deleteAll()

}