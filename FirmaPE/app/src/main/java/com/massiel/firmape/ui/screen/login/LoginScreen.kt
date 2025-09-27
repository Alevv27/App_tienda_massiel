package com.massiel.firmape.ui.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    vm: LoginViewModel = viewModel(),
    onSuccess: (com.massiel.firmape.data.model.Usuario) -> Unit
) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    LaunchedEffect(state.user) { state.user?.let(onSuccess) }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(email, { email = it }, label={ Text("Correo") }, singleLine=true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(pass, { pass = it }, label={ Text("Contraseña") }, singleLine=true)
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { vm.login(email.trim(), pass) },
            enabled = !state.loading
        ) { Text(if(state.loading) "Cargando..." else "Iniciar") }

        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}