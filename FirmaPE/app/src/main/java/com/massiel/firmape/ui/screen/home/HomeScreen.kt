package com.massiel.firmape.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.massiel.firmape.data.model.Usuario

@Composable
fun HomeScreen(user: Usuario, onGoDocs: (String?) -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Hola, ${user.nombre}", style = MaterialTheme.typography.headlineSmall)
        Text("Perfil: ${user.perfil}")

        Spacer(Modifier.height(16.dp))
        // Gestión (todos)
        Button(onClick = { onGoDocs(null) }, modifier = Modifier.fillMaxWidth()) { Text("Gestión de Documentos") }
        Spacer(Modifier.height(8.dp))

        // Firmar: solo ADMIN o FIRMANTE
        if (user.perfil == "ADMIN" || user.perfil == "FIRMANTE") {
            Button(onClick = { onGoDocs("PENDIENTE") }, modifier = Modifier.fillMaxWidth()) { Text("Firmar documentos") }
            Spacer(Modifier.height(8.dp))
        }
        // Administración: solo ADMIN
        if (user.perfil == "ADMIN") {
            Button(onClick = { /* navegar admin más adelante */ }, modifier = Modifier.fillMaxWidth()) { Text("Administración") }
        }
    }
}