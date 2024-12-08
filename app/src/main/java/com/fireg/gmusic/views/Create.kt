package com.fireg.gmusic.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fireg.gmusic.database.dao.Music
import com.fireg.gmusic.viewmodels.MusicViewModel
import com.fireg.gmusic.viewmodels.UserViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Create(
    viewModel: MusicViewModel,
    userViewModel: UserViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    val loggedInUser by userViewModel.loggedInUser.observeAsState(null)
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var filePath by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf<String?>(null) }
    var artistError by remember { mutableStateOf<String?>(null) }
    var fileError by remember { mutableStateOf<String?>(null) }


    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                // Copiar o arquivo para o diretório do aplicativo
                val copiedFilePath = copyFileToAppDirectory(uri, navController.context)
                if (copiedFilePath != null) {
                    filePath = copiedFilePath
                    fileError = null
                } else {
                    fileError = "Falha ao selecionar arquivo"
                    Log.e("Create", "Falha ao copiar o arquivo")
                }
            }
        }
    }

    // Validation functions
    fun validateTitle(): Boolean {
        titleError = when {
            title.isBlank() -> "Título não pode estar em branco"
            title.length < 2 -> "Título deve ter pelo menos 2 caracteres"
            else -> null
        }
        return titleError == null
    }

    fun validateArtist(): Boolean {
        artistError = when {
            artist.isBlank() -> "Artista não pode estar em branco"
            artist.length < 2 -> "Artista deve ter pelo menos 2 caracteres"
            else -> null
        }
        return artistError == null
    }

    fun validateFile(): Boolean {
        fileError = if (filePath.isEmpty()) "Selecione um arquivo de música" else null
        return fileError == null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voltar", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Screen Title
            Text(
                text = "Adicionar Música",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Title TextField
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = null
                },
                label = { Text("Título", color = Color.White) },
                singleLine = true,
                isError = titleError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF1DB954),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                trailingIcon = {
                    if (titleError != null) {
                        Icon(Icons.Default.Warning, contentDescription = "Erro", tint = Color.Red)
                    }
                }
            )

            // Title Error Text
            if (titleError != null) {
                Text(
                    text = titleError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            // Artist TextField
            OutlinedTextField(
                value = artist,
                onValueChange = {
                    artist = it
                    artistError = null
                },
                label = { Text("Artista", color = Color.White) },
                singleLine = true,
                isError = artistError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF1DB954),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                trailingIcon = {
                    if (artistError != null) {
                        Icon(Icons.Default.Warning, contentDescription = "Erro", tint = Color.Red)
                    }
                }
            )

            // Artist Error Text
            if (artistError != null) {
                Text(
                    text = artistError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            // Description TextField
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descrição", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF1DB954),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // File Selection Button
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "audio/*"
                    }
                    filePickerLauncher.launch(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954) // Spotify green
                )
            ) {
                Text(
                    text = if (filePath.isEmpty()) "Escolher Arquivo" else "Arquivo Selecionado",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            // File Error Text
            if (fileError != null) {
                Text(
                    text = fileError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            // Save Button
            Button(
                onClick = {
                    val isTitleValid = validateTitle()
                    val isArtistValid = validateArtist()
                    val isFileValid = validateFile()

                    if (isTitleValid && isArtistValid && isFileValid) {
                        val music = Music(
                            title = title,
                            artist = artist,
                            description = description,
                            filePath = filePath,
                            sharedBy = loggedInUser?.username ?: "" // Substitua pelo usuário atual
                        )
                        viewModel.addMusic(music)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954) // Spotify green
                )
            ) {
                Text("Salvar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Função para copiar arquivo para o diretório do aplicativo
private fun copyFileToAppDirectory(uri: Uri, context: Context): String? {
    return try {
        val musicDir = context.getExternalFilesDir("music")
        if (musicDir != null && !musicDir.exists()) {
            musicDir.mkdirs()
        }

        val destinationFile = File(musicDir, "${System.currentTimeMillis()}.mp3")
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        destinationFile.absolutePath // Retorna o caminho absoluto do arquivo salvo
    } catch (e: Exception) {
        Log.e("Create", "Erro ao copiar arquivo", e)
        null
    }
}