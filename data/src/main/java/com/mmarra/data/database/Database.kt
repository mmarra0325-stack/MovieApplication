package com.mmarra.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mmarra.data.database.entity.FavoriteEntity
import com.mmarra.data.database.mappers.Converters

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}