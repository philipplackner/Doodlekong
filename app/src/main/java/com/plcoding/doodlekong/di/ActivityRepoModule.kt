package com.plcoding.doodlekong.di

import com.plcoding.doodlekong.repository.DefaultSetupRepository
import com.plcoding.doodlekong.repository.SetupRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped


@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRepoModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun provideSetupRepository(defaultSetupRepository: DefaultSetupRepository): SetupRepository
}