package com.pocholomia.itunes.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.pocholomia.itunes.data.local.TrackDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun trackDatabase(
        @ApplicationContext context: Context
    ): TrackDatabase = Room.databaseBuilder(
        context,
        TrackDatabase::class.java,
        "track.db"
    ).build()

    @Singleton
    @Provides
    fun sharedPreferences( @ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("itunestrack", Context.MODE_PRIVATE)

}