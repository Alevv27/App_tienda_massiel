package com.massiel.firmape.ui.screen.docs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UploadLocalScreen(
    vm: DocsLocalViewModel = viewModel(),
    onDone: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Otros") }
    var pickedUri by remember { mutableStateOf<Uri?>(null) }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        pickedUri = uri
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Subir documento (Local)", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(titulo, { titulo = it }, label = { Text("Título") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        TipoDropdown(selected = tipo, onSelect = { tipo = it })
        Spacer(Modifier.height(12.dp))

        Button(onClick = { picker.launch(arrayOf("application/pdf", "image/*")) }) {
            Text(if (pickedUri == null) "Elegir archivo" else "Cambiar archivo")
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { pickedUri?.let { vm.subir(it, titulo, tipo); onDone() } },
            enabled = pickedUri != null
        ) { Text("Guardar") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TipoDropdown(selected: String, onSelect: (String) -> Unit) {
    val opciones = listOf("Otros","Carta","Memo","Boleta")
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            value = selected, onValueChange = {},
            readOnly = true, label = { Text("Tipo de documento") },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            opciones.forEach { opt ->
                DropdownMenuItem(text = { Text(opt) }, onClick = { onSelect(opt); expanded = false })
            }
        }
    }
}