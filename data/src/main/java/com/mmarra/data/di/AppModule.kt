package com.mmarra.data.di

import com.mmarra.data.repository.DatabaseFavoritesRepository
import com.mmarra.data.repository.LocalFiltersRepository
import com.mmarra.data.repository.LocalNotificationRepository
import com.mmarra.data.repository.LocalProfileRepository
import com.mmarra.data.repository.NetworkMovieRepository
import com.mmarra.domain.repository.FavoritesRepository
import com.mmarra.domain.repository.FiltersRepository
import com.mmarra.domain.repository.MovieRepository
import com.mmarra.domain.repository.NotificationRepository
import com.mmarra.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        networkMovieRepository: NetworkMovieRepository
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindFiltersRepository(
        filtersRepository: LocalFiltersRepository,
    ): FiltersRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteMovieRepository(
        databaseFavoritesRepository: DatabaseFavoritesRepository,
    ): FavoritesRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        localProfileRepository: LocalProfileRepository,
    ): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindProfileNotificationRepository(
        localNotificationRepository: LocalNotificationRepository,
    ): NotificationRepository
}