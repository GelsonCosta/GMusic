package com.fireg.gmusic



import Player
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fireg.gmusic.viewmodels.MusicViewModel
import com.fireg.gmusic.viewmodels.UserViewModel
import com.fireg.gmusic.views.Create
import com.fireg.gmusic.views.Home
import com.fireg.gmusic.views.Login
import com.fireg.gmusic.views.Register


@Composable
fun GMusicNavigation(
    musicViewModel: MusicViewModel,
    userViewModel: UserViewModel
) {
    val navController = rememberNavController()
    val startDestination = if (userViewModel.loggedInUser.value != null) "feed" else "login"
    NavHost(navController = navController, startDestination = "splash") {

        // Login Screen
        composable("login") {
            Login(
                viewModel = userViewModel,
                onLoginSuccess = {
                    navController.navigate("feed") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegister = { navController.navigate("register") }
            )
        }

        // Register Screen
        composable("register") {
            Register(
                viewModel = userViewModel,
                onBackToLogin = { navController.popBackStack() }
            )
        }

        // Feed Screen
        composable("feed") {
            Home(
                viewModel = musicViewModel,
                navController = navController,
                userViewModel = userViewModel
            )
        }

        composable("splash") {
            SplashScreen(onSplashFinished = {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            })


        }

        // Add Music Screen
        composable("create") {
            Create(
                viewModel = musicViewModel,
                navController = navController,
                userViewModel = userViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("player/{musicId}") { backStackEntry ->
            val musicId = backStackEntry.arguments?.getString("musicId")?.toIntOrNull()
            if (musicId != null) {
                Player(
                    viewModel = musicViewModel,
                    musicId = musicId,
                    onBack = { navController.popBackStack() }
                )
            }
        }

    }
}

