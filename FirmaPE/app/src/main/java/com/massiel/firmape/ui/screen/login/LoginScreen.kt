package com.massiel.firmape.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation



@Composable
fun LoginScreen(
    vm: LoginViewModel = viewModel(),
    onSuccess: (com.massiel.firmape.data.model.Usuario) -> Unit
) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    LaunchedEffect(state.user) { state.user?.let(onSuccess) }

    Column(
        modifier = Modifier
            .fillMaxSize() //Ahora ocupa toda la pantalla
            .background(Color(0xFF652B56)), // Fondo nuevo
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cuadro de bienvenida con imagen y texto
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black, shape = RoundedCornerShape(20.dp))
                .padding(32.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){

                Spacer(modifier = Modifier.width(16.dp))
            }

            Text("¡Bienvenido a FirmaPE!",
                color = Color.White, style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.height(80.dp)) // Espaciado entre el cuadro de bienvenida y los campos de entrada

        // Cuadro de texto para el correo
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(Modifier.height(16.dp)) // Espaciado entre los campos de entrada

        // Cuadro de texto para la contraseña
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(Modifier.height(24.dp)) // Espaciado entre el campo de contraseña y el botón

        // Botón de iniciar sesión con color rosado
        Button(
            onClick = { vm.login(email.trim(), pass) },
            enabled = !state.loading,
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .padding(2.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8789E)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(if (state.loading) "Cargando..." else "Iniciar", color = Color(0xFFF0F0F0))
        }

        // Mensaje de error
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium) }

        Spacer(Modifier.height(12.dp)) // Reduce el espacio entre los campos
        Spacer(Modifier.height(32.dp)) // Aumenta el espacio entre el cuadro de bienvenida y los campos

    }
}
