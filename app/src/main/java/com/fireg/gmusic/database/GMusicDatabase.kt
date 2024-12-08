package com.fireg.gmusic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fireg.gmusic.database.dao.Music
import com.fireg.gmusic.database.dao.MusicDao
import com.fireg.gmusic.database.dao.User
import com.fireg.gmusic.database.dao.UserDao


@Database(entities = [Music::class, User::class], version = 1)
abstract class GMusicDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: GMusicDatabase? = null

        fun getInstance(context: Context): GMusicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GMusicDatabase::class.java,
                    "music_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
