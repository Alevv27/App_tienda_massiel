package com.massiel.firmape.ui.screen.home

import androidx.compose.foundation.shape.CircleShape
import com.massiel.firmape.R
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.massiel.firmape.data.model.Usuario


@Composable
fun AhorraTiempoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterStart)
                    .background(Color.Gray, shape = CircleShape)
            )
            Text(
                text = "¡Ahorra tiempo!",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
            Image(
                painter = painterResource(id = R.drawable.robotcito),
                contentDescription = "Ahorra tiempo",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun HomeScreen(
    user: Usuario,
    onGoDocs: (String?) -> Unit,
    onGoUpload: () -> Unit,//navegar en subir documento
    onGoAdmin: () -> Unit,      // 👈 NUEVO
    onLogout: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4B224B))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Text(
                text = "Hola, ${user.nombre}",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                text = "Perfil: ${user.perfil}",
                color = Color.White
            )

            Spacer(Modifier.height(24.dp))

            // 🔹 Aquí insertamos la tarjeta de Ahorra Tiempo
            AhorraTiempoCard()

            Spacer(Modifier.height(24.dp))

            // Gestión
            MenuCard("Gestión Documentos") {  onGoUpload() }//lleva directo a subir documento
            Spacer(Modifier.height(12.dp))

            // Firmar
            if (user.perfil == "ADMIN" || user.perfil == "FIRMANTE") {
                MenuCard("Firmar documentos") { onGoDocs("PENDIENTE") }
                Spacer(Modifier.height(12.dp))
            }

            // Administración
            if (user.perfil == "ADMIN") {
                MenuCard("Administración") { onGoAdmin()}
            }
        }

        FloatingActionButton(
            onClick = { onLogout() },
            containerColor = Color(0xFFE8789E),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Cerrar\nSesión")
        }
    }
}

@Composable
fun MenuCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .background(
                        color = Color(0xFFE8789E),
                        shape = RoundedCornerShape(12.dp)
                    )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


