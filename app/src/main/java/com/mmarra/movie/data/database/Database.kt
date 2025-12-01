package com.mmarra.movie.data.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mmarra.movie.data.database.entity.FavoriteEntity
import com.mmarra.movie.data.database.mappers.Converters

@RequiresApi(Build.VERSION_CODES.O)
@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}