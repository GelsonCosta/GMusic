package com.fireg.gmusic.database.repositories

import com.fireg.gmusic.database.dao.Music
import com.fireg.gmusic.database.dao.MusicDao
import kotlinx.coroutines.flow.Flow

class MusicRepository(private val musicDao: MusicDao) {
    val allMusic: Flow<List<Music>> = musicDao.getAll()

    suspend fun insert(music: Music) {
        musicDao.insert(music)
    }

    suspend fun delete(music: Music) {
        musicDao.delete(music)
    }
}
