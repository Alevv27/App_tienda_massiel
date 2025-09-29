package com.massiel.firmape.ui.screen.docs


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.massiel.firmape.util.openFile
import com.massiel.firmape.util.shareFile

@Composable
fun DocsScreen(
    vm: DocsLocalViewModel = viewModel(),
    estado: String? = null,
    signerName: String,
    onUpload: () -> Unit
) {
    val state by vm.state.collectAsState()
    val ctx = LocalContext.current
    LaunchedEffect(estado) { vm.cargar(estado) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.cargar(null) }) { Text("Todos") }
            Button(onClick = { vm.cargar("PENDIENTE") }) { Text("Pendientes") }
            Button(onClick = { vm.cargar("FIRMADO") }) { Text("Firmados") }
            Button(onClick = { vm.cargar("RECHAZADO") }) { Text("Rechazados") }
            Spacer(Modifier.weight(1f))
            //Button(onClick = onUpload) { Text("Subir") }
        }
        Spacer(Modifier.height(8.dp))

        if (state.items.isEmpty()) {
            Text("No hay documentos")
        } else {
            LazyColumn {
                items(state.items) { d ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(d.titulo, style = MaterialTheme.typography.titleMedium)
                            Text("Tipo: ${d.tipo} • Tamaño: ${"%.2f".format(d.sizeBytes/1024f/1024f)} MB")
                            Text("Estado: ${d.estado}")
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(onClick = { openFile(ctx, d.path) }) { Text("Abrir") }
                                TextButton(onClick = { shareFile(ctx, d.path) }) { Text("Compartir") }
                                if (d.estado == "PENDIENTE") {
                                    TextButton(onClick = { vm.firmar(d.id,signerName) }) { Text("Firmar") }
                                    TextButton(onClick = { vm.rechazar(d.id,signerName) }) { Text("Rechazar") }
                                }
                                TextButton(onClick = { vm.eliminar(d.id) }) { Text("Eliminar") }
                            }
                        }
                    }
                }
            }
        }

        state.toast?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
    }
}