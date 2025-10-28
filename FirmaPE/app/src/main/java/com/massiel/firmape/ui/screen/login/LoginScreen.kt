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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.font.FontWeight
import com.massiel.firmape.R






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
                .fillMaxWidth(0.9f)
                .background(Color.Black, shape = RoundedCornerShape(38.dp))
                .padding(horizontal = 32.dp, vertical = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Imagen del robotcito
                Image(
                    painter = painterResource(id = R.drawable.robotcito),
                    contentDescription = "Robotcito",
                    modifier = Modifier
                        .size(100.dp) // 🔹 Tamaño ajustable del icono
                        .padding(end = 35.dp) // 🔹 Espacio entre la imagen y el texto
                )

                // Texto de bienvenida
                Text(
                    text = "¡Bienvenido!",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize * 0.8f,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(Modifier.height(45.dp)) // Espaciado entre el cuadro de bienvenida y los campos de entrada


        Spacer(Modifier.height(45.dp)) // Espaciado entre el cuadro de bienvenida y los campos de entrada


        // Cuadro de texto para el correo
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {
                if (email.isEmpty())
                Text(
                    text = "Correo",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.0f, // un poco más grande
                        fontWeight = FontWeight.Bold // más grueso
                    )
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Gray,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.2f, // texto más grande
                fontWeight = FontWeight.Bold // letra más gruesa
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 9.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp)), // fondo blanco
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,   // 👈 línea invisible
                unfocusedIndicatorColor = Color.Transparent  // 👈 línea invisible
            )

        )


        Spacer(Modifier.height(50.dp)) // Espaciado entre los campos de entrada

        // Cuadro de texto para la contraseña
        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = {
                if (pass.isEmpty())
                Text(
                    text = "Contraseña",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.0f,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = Color.Gray,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.2f,
                fontWeight = FontWeight.Bold
            ),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 9.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,   // 👈 línea invisible
                unfocusedIndicatorColor = Color.Transparent  // 👈 línea invisible
            )

        )


        Spacer(Modifier.height(45.dp)) // Espaciado entre el campo de contraseña y el botón

        // Botón de iniciar sesión con color rosado más intenso y texto más grande
        Button(
            onClick = { vm.login(email.trim(), pass) },
            enabled = !state.loading,
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
                .padding(2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEB5A8E) // 💗 un poco más fuerte que el anterior (E8789E)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = if (state.loading) "Cargando..." else "Iniciar",
                color = Color(0xFFF0F0F0),
                style = MaterialTheme.typography.bodyLarge.copy( // 👈 texto un poco más grande
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.2f
                )
            )
        }


        // Mensaje de error
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium) }

        Spacer(Modifier.height(12.dp)) // Reduce el espacio entre los campos
        Spacer(Modifier.height(32.dp)) // Aumenta el espacio entre el cuadro de bienvenida y los campos

    }
}
