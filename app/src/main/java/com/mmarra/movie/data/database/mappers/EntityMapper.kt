package com.mmarra.movie.data.database.mappers

import com.mmarra.movie.data.database.entity.FavoriteEntity
import com.mmarra.movie.domain.model.Movie

fun Movie.toFavoriteEntity(): FavoriteEntity =
    FavoriteEntity(
        id = id,
        movieJson = Converters.gson.toJson(this)
    )

fun FavoriteEntity.toMovie(): Movie =
    Converters.gson.fromJson(movieJson, Movie::class.java)