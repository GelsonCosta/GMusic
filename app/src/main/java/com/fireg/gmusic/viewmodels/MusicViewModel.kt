package com.fireg.gmusic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fireg.gmusic.database.GMusicDatabase
import com.fireg.gmusic.database.dao.Music
import com.fireg.gmusic.database.repositories.MusicRepository
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MusicRepository

    val allMusic: LiveData<List<Music>>

    init {
        val musicDao = GMusicDatabase.getInstance(application).musicDao()
        repository = MusicRepository(musicDao)
        allMusic = repository.allMusic.asLiveData()
    }

    fun addMusic(music: Music) = viewModelScope.launch {
        repository.insert(music)
    }

    fun deleteMusic(music: Music) = viewModelScope.launch {
        repository.delete(music)
    }
}
