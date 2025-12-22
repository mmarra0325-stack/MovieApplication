package com.mmarra.data.database.mappers

import com.mmarra.data.database.entity.FavoriteEntity
import com.mmarra.domain.model.Movie

fun Movie.toFavoriteEntity(): FavoriteEntity =
    FavoriteEntity(
        id = id,
        movieJson = Converters.gson.toJson(this)
    )

fun FavoriteEntity.toMovie(): Movie =
    Converters.gson.fromJson(movieJson, Movie::class.java)