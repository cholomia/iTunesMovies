package com.pocholomia.itunes.di

import com.pocholomia.itunes.data.repository.TrackUseCaseImpl
import com.pocholomia.itunes.domain.usecase.TrackUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @ViewModelScoped
    @Binds
    abstract fun trackUseCase(impl: TrackUseCaseImpl): TrackUseCase

}