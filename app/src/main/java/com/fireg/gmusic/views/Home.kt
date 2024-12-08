package com.fireg.gmusic.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fireg.gmusic.R
import com.fireg.gmusic.viewmodels.MusicViewModel
import com.fireg.gmusic.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    viewModel: MusicViewModel,
    navController: NavController,
    userViewModel: UserViewModel,
) {
    val musicList by viewModel.allMusic.observeAsState(emptyList())
    val loggedInUser by userViewModel.loggedInUser.observeAsState(null)
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }


    val filteredMusicList = remember(musicList, searchQuery) {
        musicList.filter { music ->
            music.title.contains(searchQuery, ignoreCase = true) ||
                    music.artist.contains(searchQuery, ignoreCase = true) ||
                    music.sharedBy.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = Color(0xFF121212),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            loggedInUser?.username ?: "GMusic",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    ),
                    actions = {
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Opções", tint = Color.White)
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(Color(0xFF282828))
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Terminar Sessão",
                                            color = Color.White
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.ExitToApp,
                                            contentDescription = "Terminar Sessão",
                                            tint = Color.White
                                        )
                                    },
                                    onClick = {
                                        userViewModel.logout()
                                        navController.navigate("login") {
                                            popUpTo(navController.graph.startDestinationId) {
                                                inclusive = true
                                            }
                                        }
                                        expanded = false
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.White,
                                        leadingIconColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                )

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Pesquisar músicas", color = Color.Gray) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = Color.White)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar", tint = Color.White)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF1DB954),
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,

                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("create") },
                containerColor = Color(0xFF1DB954),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Música")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF121212))
        ) {

            if (filteredMusicList.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhuma música encontrada",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }


            items(filteredMusicList.reversed()) { music ->
                MusicCard(
                    title = music.title,
                    artist = music.artist,
                    sharedBy = music.sharedBy,
                    onItemClick = { navController.navigate("player/${music.id}") }
                )
            }
        }
    }
}


@Composable
fun MusicCard(
    title: String,
    artist: String,
    sharedBy: String,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onItemClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF282828)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.album),
                contentDescription = title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Music Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = artist,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.LightGray
                    )
                )
                Text(
                    text = "Compartilhado por $sharedBy",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }

            // Play Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1DB954)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Reproduzir",
                    tint = Color.Black
                )
            }
        }
    }
}