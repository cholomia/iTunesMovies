package com.pocholomia.itunes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pocholomia.itunes.domain.model.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE trackId=:trackId")
    suspend fun remoteKeysTrackId(trackId: Long): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()

}