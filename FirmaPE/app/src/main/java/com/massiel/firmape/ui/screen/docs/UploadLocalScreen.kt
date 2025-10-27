package com.massiel.firmape.ui.screen.docs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun UploadLocalScreen(
    vm: DocsLocalViewModel = viewModel(),
    onDone: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Otros") }
    var pickedUri by remember { mutableStateOf<Uri?>(null) }

    val puedeGuardar = pickedUri != null && titulo.isNotBlank()
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        pickedUri = uri
    }

    // Colores para igualar el look & feel
    val fondo = Color(0xFF4B224B)
    val rosa = Color(0xFFE8789E)

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
                onClick = {  onDone() },
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
        }

        // Card del formulario
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF7FA))
        ) {
            Column(Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                TipoDropdown(selected = tipo, onSelect = { tipo = it })

                Spacer(Modifier.height(14.dp))

                Button(
                    onClick = { picker.launch(arrayOf("application/pdf", "image/*")) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = rosa),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = if (pickedUri == null) "Elegir archivo" else "Cambiar archivo",
                        color = Color.White
                    )
                }

                if (pickedUri != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = pickedUri.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF5A5A5A)
                    )
                }

                Spacer(Modifier.height(18.dp))

                Button(
                    onClick = {
                        pickedUri?.let { uri ->
                            vm.subir(uri, titulo.trim(), tipo)
                            // Limpiar para permitir subir más
                            titulo = ""
                            tipo = "Otros"
                            pickedUri = null
                        }
                    },
                    enabled = puedeGuardar,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = rosa),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Guardar", color = Color.White)
                }

                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onDone,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TipoDropdown(selected: String, onSelect: (String) -> Unit) {
    val opciones = listOf("Otros", "Carta", "Memo", "Boleta")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo de documento") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = {
                        onSelect(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}
