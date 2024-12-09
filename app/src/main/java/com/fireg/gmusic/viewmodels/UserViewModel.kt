package com.fireg.gmusic.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.fireg.gmusic.database.GMusicDatabase
import com.fireg.gmusic.database.dao.User
import com.fireg.gmusic.database.dao.UserDao
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao: UserDao = GMusicDatabase.getInstance(application).userDao()

    private val _loggedInUser = MutableLiveData<User?>()
    val loggedInUser: LiveData<User?> = _loggedInUser

    fun login(email: String, password: String, rememberMe: Boolean,  onError: (String) -> Unit) = viewModelScope.launch {
        val user = userDao.getUserByEmail(email = email)
        if (user != null && user.password == password) {
            _loggedInUser.postValue(user)
            userDao.updateRememberMe(user.id, rememberMe)
        } else {
            onError("Email ou Senha Errados!")
            return@launch
        }
    }

    fun logout() = viewModelScope.launch {
        _loggedInUser.value?.let { user ->
            userDao.updateRememberMe(user.id, false)
        }
        _loggedInUser.postValue(null)
    }

    fun register(username: String, email: String, password: String,  onError: (String) -> Unit) = viewModelScope.launch {
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser == null) {
            val user = User(username = username,email = email, password = password)
            userDao.insertUser(user)
            onError("")
        } else {
            onError("Email já cadastrado!")
            return@launch
        }
    }

    fun checkRememberedUser() = viewModelScope.launch {
        val rememberedUser = userDao.getRememberedUser()
        _loggedInUser.postValue(rememberedUser)
    }
}
