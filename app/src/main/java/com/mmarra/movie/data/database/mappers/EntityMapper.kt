package com.mmarra.movie.data.database.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.mmarra.movie.data.database.entity.FavoriteEntity
import com.mmarra.movie.domain.model.Movie

@RequiresApi(Build.VERSION_CODES.O)
fun Movie.toFavoriteEntity(): FavoriteEntity =
    FavoriteEntity(
        id = id,
        movieJson = Converters.json.encodeToString(this),
    )

@RequiresApi(Build.VERSION_CODES.O)
fun FavoriteEntity.toMovie(): Movie = Converters.json.decodeFromString(movieJson)