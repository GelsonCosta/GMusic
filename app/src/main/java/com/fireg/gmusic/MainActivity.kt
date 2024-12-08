package com.fireg.gmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fireg.gmusic.ui.theme.GMusicTheme
import com.fireg.gmusic.viewmodels.MusicViewModel
import com.fireg.gmusic.viewmodels.UserViewModel
import com.fireg.gmusic.views.Login

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: UserViewModel by viewModels()
        val MusicviewModel: MusicViewModel by viewModels()

        viewModel.checkRememberedUser()
        setContent{
            GMusicNavigation(musicViewModel = MusicviewModel,userViewModel = viewModel)


        }
    }
}

