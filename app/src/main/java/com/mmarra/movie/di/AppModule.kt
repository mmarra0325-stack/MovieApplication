package com.mmarra.movie.di

import com.mmarra.movie.data.repository.MovieRepository
import com.mmarra.movie.data.repository.NetworkMovieRepository
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

}