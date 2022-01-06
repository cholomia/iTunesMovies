package com.pocholomia.itunes.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val trackId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)