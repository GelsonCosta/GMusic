package com.fireg.gmusic.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fireg.gmusic.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    viewModel: UserViewModel,
    onLoginSuccess: () -> Unit,
    onRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var loginFailed by remember { mutableStateOf(false) }
    val loggedInUser by viewModel.loggedInUser.observeAsState()

    // Validation functions
    fun validateEmail(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")
        emailError = when {
            email.isBlank() -> "E-mail não pode estar em branco"
            !email.matches(emailRegex) -> "E-mail inválido"
            else -> null
        }
        return emailError == null
    }

    fun validatePassword(): Boolean {
        passwordError = when {
            password.isBlank() -> "Senha não pode estar em branco"
            password.length < 6 -> "Senha deve ter pelo menos 6 caracteres"
            else -> null
        }
        return passwordError == null
    }


    LaunchedEffect(email, password) {
        loginFailed = false
    }


    LaunchedEffect(loggedInUser) {
        if (loggedInUser != null) {
            onLoginSuccess()
        } else if (loggedInUser == null && email.isNotBlank() && password.isNotBlank()) {
            loginFailed = true
        }
    }

    Scaffold(
        containerColor = Color(0xFF121212) // Spotify dark background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Title
            Text(
                text = "G Music",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                    loginFailed = false
                },
                label = { Text("E-mail", color = Color.White) },
                singleLine = true,
                isError = emailError != null || loginFailed,
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
                    if (emailError != null || loginFailed) {
                        Icon(Icons.Default.Warning, contentDescription = "Erro", tint = Color.Red)
                    }
                }
            )

            // Email Error Text
            if (emailError != null) {
                Text(
                    text = emailError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                    loginFailed = false
                },
                label = { Text("Senha", color = Color.White) },
                singleLine = true,
                isError = passwordError != null || loginFailed,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                    Row {
                        if (passwordError != null || loginFailed) {
                            Icon(Icons.Default.Warning, contentDescription = "Erro", tint = Color.Red)
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = if (passwordVisible) "Esconder senha" else "Mostrar senha",
                                tint = Color.White
                            )
                        }
                    }
                }
            )

            // Password Error Text
            if (passwordError != null) {
                Text(
                    text = passwordError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            // Login Failed Error Message
            if (loginFailed) {
                Text(
                    text = "E-mail ou senha inválidos",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Remember Me Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF1DB954),
                        uncheckedColor = Color.White
                    )
                )
                Text("Lembrar-me", color = Color.White)
            }

            // Login Button
            Button(
                onClick = {
                    val isEmailValid = validateEmail()
                    val isPasswordValid = validatePassword()

                    if (isEmailValid && isPasswordValid) {
                        viewModel.login(email, password, rememberMe)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954) // Spotify green
                )
            ) {
                Text("Entrar", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            // Register Button
            TextButton(
                onClick = onRegister,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Criar conta", color = Color.White)
            }
        }
    }
}