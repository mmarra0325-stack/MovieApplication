package com.mmarra.movie.data.database.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.mmarra.movie.domain.model.Movie
import kotlinx.serialization.json.Json
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
object Converters {

    internal val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun movieToJson(movie: Movie?): String? =
        movie?.let { json.encodeToString(it) }

    @TypeConverter
    fun jsonToMovie(src: String?): Movie? =
        src?.let { json.decodeFromString<Movie>(it) }

    @TypeConverter
    fun instantToLong(value: Instant?): Long? =
        value?.toEpochMilli()

    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let { Instant.ofEpochMilli(it) }
}