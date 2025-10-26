package com.massiel.firmape.ui.screen.docs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.massiel.firmape.util.openFile
import com.massiel.firmape.util.shareFile

@Composable
fun DocsScreen(
    navController: NavController,
    vm: DocsLocalViewModel = viewModel(),
    estado: String? = null,
    signerName: String,
    onUpload: () -> Unit
) {
    val state by vm.state.collectAsState()
    val ctx = LocalContext.current
    LaunchedEffect(estado) { vm.cargar(estado) }

    val fondo = Color(0xFF4B224B)
    val botonColor = Color(0xFFE8789E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Black, shape = RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Documentos",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { vm.cargar(null) },
                colors = ButtonDefaults.buttonColors(containerColor = botonColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) { Text("Todos", color = Color.White) }

            Button(
                onClick = { vm.cargar("PENDIENTE") },
                colors = ButtonDefaults.buttonColors(containerColor = botonColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) { Text("Pendientes", color = Color.White) }

            Button(
                onClick = { vm.cargar("FIRMADO") },
                colors = ButtonDefaults.buttonColors(containerColor = botonColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) { Text("Firmados", color = Color.White) }

            Button(
                onClick = { vm.cargar("RECHAZADO") },
                colors = ButtonDefaults.buttonColors(containerColor = botonColor),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            ) { Text("Rechazados", color = Color.White) }
        }

        Spacer(Modifier.height(24.dp))

        if (state.items.isEmpty()) {
            Text("No hay documentos", color = Color.White)
        } else {
            LazyColumn {
                items(state.items) { d ->
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = botonColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                d.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Text(
                                "Tipo: ${d.tipo} • Tamaño: ${
                                    "%.2f".format(d.sizeBytes / 1024f / 1024f)
                                } MB",
                                color = Color.White
                            )
                            Text("Estado: ${d.estado}", color = Color.White)

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextButton(onClick = { openFile(ctx, d.path) }) {
                                    Text("Abrir", color = Color.White)
                                }
                                TextButton(onClick = { shareFile(ctx, d.path) }) {
                                    Text("Compartir", color = Color.White)
                                }
                                if (d.estado == "PENDIENTE") {
                                    TextButton(onClick = { vm.firmar(d.id, signerName) }) {
                                        Text("Firmar", color = Color.White)
                                    }
                                    TextButton(onClick = { vm.rechazar(d.id, signerName) }) {
                                        Text("Rechazar", color = Color.White)
                                    }
                                }
                            }

                            TextButton(
                                onClick = { vm.eliminar(d.id) },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Eliminar", color = Color.White)
                            }
                        }
                    }
                }
            }

        }

        state.toast?.let { Text(it, color = Color.White) }
    }
}



