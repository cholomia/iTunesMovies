package com.pocholomia.itunes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pocholomia.itunes.data.local.dao.RemoteKeysDao
import com.pocholomia.itunes.data.local.dao.TrackDao
import com.pocholomia.itunes.domain.model.RemoteKeys
import com.pocholomia.itunes.domain.model.Track

@Database(
    entities = [
        Track::class,
        RemoteKeys::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TrackDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun remoteKeysDao(): RemoteKeysDao

}