package com.mmarra.movie.di

import com.mmarra.domain.repository.AlarmManager
import com.mmarra.movie.services.AlarmManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmModule {

    @Binds
    @Singleton
    abstract fun bindAlarmManager(
        impl: AlarmManagerImpl
    ): AlarmManager
}