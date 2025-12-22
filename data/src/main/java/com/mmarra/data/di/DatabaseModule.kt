package com.mmarra.data.di

import android.content.Context
import androidx.room.Room
import com.mmarra.data.database.Database
import com.mmarra.data.database.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "favorites.db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteDao(db: Database): FavoriteDao {
        return db.favoriteDao()
    }
}