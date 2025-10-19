package com.mmarra.movie.data.network

import com.mmarra.movie.data.network.dto.MovieListResponse
import com.mmarra.movie.data.network.dto.MovieObjectResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface KinopoiskApi {
    @GET("v1.4/movie")
    suspend fun getMovies(@QueryMap filters: Map<String, String>): MovieListResponse

    @GET("v1.4/movie/{id}")
    suspend fun getMovieById(@Path("id") id: Int): MovieObjectResponse

    @GET("v1.4/movie/search")
    suspend fun searchMovies(
        @Query("query") query: String,
        @QueryMap params: Map<String, String> = emptyMap()
    ): MovieListResponse
}
