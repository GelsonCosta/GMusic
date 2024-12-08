package com.fireg.gmusic.database.dao

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity
data class Music(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val artist: String,
    val description: String,
    val filePath: String,
    val sharedBy: String
)

@Dao
interface MusicDao {
    @Query("SELECT * FROM Music")
    fun getAll(): Flow<List<Music>>

    @Insert
    suspend fun insert(music: Music)

    @Delete
    suspend fun delete(music: Music)
}


