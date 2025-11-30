package com.massiel.firmape.ui.screen.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onBack: () -> Unit,
    vm: AdminViewModel = viewModel()
) {
    LaunchedEffect(Unit) { vm.loadAll() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administración") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (vm.loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
            }

            vm.error?.let { msg ->
                Text(msg, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            vm.success?.let { msg ->
                Text(msg, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
            }

            // ===== FORM CREAR USUARIO =====
            Text("Crear usuario", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            var nombre by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }

            // Datos del VM
            val perfiles = vm.perfiles
            val empresas = vm.empresas

            // Listas de texto *seguras* (sin nulos)
            val perfilLabels: List<String?> = perfiles.map { it.codigo ?: "Sin código" }
            // Usa "nombre" o "name" según cómo esté tu data class Empresa
            val empresaLabels: List<String?> = empresas.map { it.name ?: "Massiel" }

            var selectedPerfilIndex by remember { mutableStateOf(0) }
            var selectedEmpresaIndex by remember { mutableStateOf(0) }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))

            // Perfil
            if (perfilLabels.isNotEmpty()) {
                Text("Perfil", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                DropdownSelector(
                    items = perfilLabels,
                    selectedIndex = selectedPerfilIndex,
                    onSelectedIndexChange = { selectedPerfilIndex = it }
                )
                Spacer(Modifier.height(4.dp))
            }

            // Empresa
            if (empresaLabels.isNotEmpty()) {
                Text("Empresa", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                DropdownSelector(
                    items = empresaLabels,
                    selectedIndex = selectedEmpresaIndex,
                    onSelectedIndexChange = { selectedEmpresaIndex = it }
                )
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    val perfilId = perfiles.getOrNull(selectedPerfilIndex)?.id ?: 1
                    val empresaId = empresas.getOrNull(selectedEmpresaIndex)?.id ?: 1

                    vm.createUsuario(
                        nombre = nombre,
                        email = email,
                        password = password,
                        perfilId = perfilId,
                        empresaId = empresaId
                    )

                    // limpiar campos
                    nombre = ""
                    email = ""
                    password = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = nombre.isNotBlank()
                        && email.isNotBlank()
                        && password.isNotBlank()
                        && perfiles.isNotEmpty()
                        && empresas.isNotEmpty()
            ) {
                Text("Crear usuario")
            }

            Spacer(Modifier.height(16.dp))

            // ===== LISTA DE USUARIOS =====
            Text("Usuarios", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            LazyColumn {
                items(vm.usuarios) { u ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(u.nombre ?: "", style = MaterialTheme.typography.bodyLarge)
                                Text(u.email ?: "", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    "Perfil: ${u.perfil ?: ""}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = if (u.activo == true) "Activo" else "Inactivo",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                TextButton(onClick = { vm.toggleActivo(u) }) {
                                    Text(if (u.activo == false) "Activar" else "Desactivar")
                                }
                                TextButton(onClick = { vm.deleteUsuario(u.id) }) {
                                    Text("Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DropdownSelector(
    items: List<String?>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // índice y texto seguros
    val safeIndex = selectedIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0))
    val currentText = items.getOrNull(safeIndex) ?: "-"

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(currentText)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEachIndexed { index, label ->
                DropdownMenuItem(
                    text = { Text(label ?: "-") },
                    onClick = {
                        onSelectedIndexChange(index)
                        expanded = false
                    }
                )
            }
        }
    }
}
