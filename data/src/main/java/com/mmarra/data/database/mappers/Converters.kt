package com.mmarra.data.database.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.mmarra.domain.model.Movie
import java.lang.reflect.Type
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
object Converters {

    private val instantAdapter = object : JsonSerializer<Instant>, JsonDeserializer<Instant> {
        override fun serialize(
            src: Instant?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ) =
            JsonPrimitive(src?.toString())

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Instant =
            Instant.parse(json!!.asString)
    }

    internal val gson = GsonBuilder()
        .registerTypeAdapter(Instant::class.java, instantAdapter)
        .create()

    @TypeConverter
    fun movieToJson(movie: Movie?): String? =
        movie?.let { gson.toJson(it) }

    @TypeConverter
    fun jsonToMovie(src: String?): Movie? =
        src?.let { gson.fromJson(it, Movie::class.java) }

    @TypeConverter
    fun instantToLong(value: Instant?): Long? =
        value?.toEpochMilli()

    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let { Instant.ofEpochMilli(it) }
}