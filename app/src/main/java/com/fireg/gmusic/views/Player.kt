import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fireg.gmusic.R
import com.fireg.gmusic.viewmodels.MusicViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Player(musicId: Int, viewModel: MusicViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val music = viewModel.allMusic.observeAsState().value?.find { it.id == musicId }

    val mediaPlayer = remember(musicId) {
        MediaPlayer().apply {
            setOnCompletionListener {
                reset()
            }
        }
    }

    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) }
    var duration by remember { mutableStateOf(0) }

    // Configuração inicial do MediaPlayer
    LaunchedEffect(music?.filePath) {
        music?.filePath?.let { filePath ->
            mediaPlayer.reset()
            mediaPlayer.setDataSource(filePath)
            mediaPlayer.prepare()
            duration = mediaPlayer.duration
        }
    }

    // Atualização do progresso da música
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            currentPosition = mediaPlayer.currentPosition
            kotlinx.coroutines.delay(1000) // Atualiza a cada segundo
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reproduzindo", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
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
            // Imagem do álbum
            Image(
                painter = painterResource(id = R.drawable.album),
                contentDescription = "Capa do Álbum",
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Título da música
            Text(
                text = music?.title ?: "Título Desconhecido",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Artista
            Text(
                text = music?.artist ?: "Artista Desconhecido",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Barra de progersso e tempo
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Slider(
                    value = currentPosition.toFloat(),
                    valueRange = 0f..duration.toFloat(),
                    onValueChange = { newPosition ->
                        currentPosition = newPosition.toInt()
                        mediaPlayer.seekTo(currentPosition)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Text(
                        text = formatTime(duration),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Controles do plaayer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {  },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.DarkGray, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Anterior",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                if (!isPlaying) {
                    IconButton(
                        onClick = {
                            mediaPlayer.start()
                            isPlaying = true
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.DarkGray, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            mediaPlayer.pause()
                            isPlaying = false
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.Gray, shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pause,
                            contentDescription = "Pausar",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                IconButton(
                    onClick = {  },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.DarkGray, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Próxima",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

// Função para formatar o tempo em mm:ss
private fun formatTime(timeInMillis: Int): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    return "%02d:%02d".format(minutes, seconds)
}
