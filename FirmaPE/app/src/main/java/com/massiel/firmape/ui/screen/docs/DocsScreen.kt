package com.massiel.firmape.ui.screen.docs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.massiel.firmape.data.model.Usuario

@Composable
fun DocsScreen(
    vm: DocsViewModel = viewModel(),
    user: Usuario,
    estado: String?
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(user.empresaId, estado) {
        vm.load(user.empresaId, estado)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Documentos ${estado ?: "Todos"}",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(8.dp))

        if (state.loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else if (state.error != null) {
            Text(state.error!!, color = MaterialTheme.colorScheme.error)
        } else {
            LazyColumn {
                items(state.items) { d ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(d.titulo, style = MaterialTheme.typography.titleMedium)
                            Text("Tipo: ${d.tipo} • Tamaño: ${d.sizeMB} MB")
                            Text("Estado: ${d.estado} • Fecha: ${d.fechaRegistro}")
                        }
                    }
                }
            }
        }
    }
}