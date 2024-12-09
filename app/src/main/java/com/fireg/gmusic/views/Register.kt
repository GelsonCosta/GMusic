package com.fireg.gmusic.views

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.fireg.gmusic.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Register(
    viewModel: UserViewModel,
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var registerError by remember {mutableStateOf(false)}

    // Validation functions
    fun validateEmail(): Boolean {
        emailError = when {
            email.isBlank() -> "Email não pode estar em branco"
            !email.contains("@") -> "Email inválido"
            else -> null
        }
        return emailError == null
    }

    fun validateUsername(): Boolean {
        usernameError = when {
            username.isBlank() -> "Usuário não pode estar em branco"
            username.length < 3 -> "Usuário deve ter pelo menos 3 caracteres"
            else -> null
        }
        return usernameError == null
    }

    fun validatePassword(): Boolean {
        passwordError = when {
            password.isBlank() -> "Senha não pode estar em branco"
            password.length < 6 -> "Senha deve ter pelo menos 6 caracteres"
            else -> null
        }
        return passwordError == null
    }

    fun validateConfirmPassword(): Boolean {
        confirmPasswordError = when {
            confirmPassword.isBlank() -> "Confirmação de senha não pode estar em branco"
            password != confirmPassword -> "As senhas não coincidem"
            else -> null
        }
        return confirmPasswordError == null
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
                text = "Criar Conta",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Username TextField
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = null
                },
                label = { Text("Usuário", color = Color.White) },
                singleLine = true,
                isError = usernameError != null,
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
                    if (usernameError != null) {
                        Icon(Icons.Default.Warning, contentDescription = "Erro", tint = Color.Red)
                    }
                }
            )

            // Username Error Text
            if (usernameError != null) {
                Text(
                    text = usernameError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text("Email", color = Color.White) },
                singleLine = true,
                isError = emailError != null,
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
                    if (emailError != null) {
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
                },
                label = { Text("Senha", color = Color.White) },
                singleLine = true,
                isError = passwordError != null,
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
                        if (passwordError != null) {
                            Icon(Icons.Default.Warning, contentDescription = "Erro", tint = Color.Red)
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Person else Icons.Default.Lock,
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

            // Confirm Password TextField
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                label = { Text("Confirmar Senha", color = Color.White) },
                singleLine = true,
                isError = confirmPasswordError != null,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                        if (confirmPasswordError != null) {
                            Icon(Icons.Default.Warning, contentDescription = "Erro", tint = Color.Red)
                        }
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Person else Icons.Default.Lock,
                                contentDescription = if (confirmPasswordVisible) "Esconder senha" else "Mostrar senha",
                                tint = Color.White
                            )
                        }
                    }
                }
            )

            // Confirm Password Error Text
            if (confirmPasswordError != null) {
                Text(
                    text = confirmPasswordError ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
            }

            // Register Button
            Button(
                onClick = {
                    val isEmailValid = validateEmail()
                    val isUsernameValid = validateUsername()
                    val isPasswordValid = validatePassword()
                    val isConfirmPasswordValid = validateConfirmPassword()

                    if (isEmailValid && isUsernameValid && isPasswordValid && isConfirmPasswordValid) {
                        viewModel.register(username, email, password,
                            onError = {
                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                registerError = it == ""
                            })
                        if(registerError){
                            onBackToLogin()
                        }


                       // registerError = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1DB954)
                )
            ) {
                Text("Criar Conta", color = Color.Black, fontWeight = FontWeight.Bold)
            }


            TextButton(
                onClick = onBackToLogin,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Voltar para Login", color = Color.White)
            }
        }
    }
}