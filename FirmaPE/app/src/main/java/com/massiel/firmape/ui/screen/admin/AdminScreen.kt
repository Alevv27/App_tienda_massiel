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
import com.massiel.firmape.data.model.Usuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    onBack: () -> Unit,
    vm: AdminViewModel = viewModel()
) {
    LaunchedEffect(Unit) { vm.loadAll() }

    // -------- estados del formulario --------
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val perfiles = vm.perfiles
    val empresas = vm.empresas

    val perfilLabels: List<String?> = perfiles.map { it.codigo ?: "Sin código" }
    val empresaLabels: List<String?> = empresas.map { it.name ?: "Massiel" }

    var selectedPerfilIndex by remember { mutableStateOf(0) }
    var selectedEmpresaIndex by remember { mutableStateOf(0) }

    // usuario que se está editando; null = modo crear
    var editingUser by remember { mutableStateOf<Usuario?>(null) }

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

            // ===== FORM CREAR / EDITAR USUARIO =====
            Text(
                text = if (editingUser == null) "Crear usuario" else "Editar usuario",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))

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
                label = {
                    Text(
                        if (editingUser == null)
                            "Password"
                        else
                            "Password"
                    )
                },
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

            val formEnabled =
                nombre.isNotBlank() &&
                        email.isNotBlank() &&
                        (editingUser != null || password.isNotBlank()) &&
                        perfiles.isNotEmpty() &&
                        empresas.isNotEmpty()

            Button(
                onClick = {
                    val perfilId = perfiles.getOrNull(selectedPerfilIndex)?.id ?: 1
                    val empresaId = empresas.getOrNull(selectedEmpresaIndex)?.id ?: 1

                    if (editingUser == null) {
                        // CREAR
                        vm.createUsuario(
                            nombre = nombre,
                            email = email,
                            password = password,
                            perfilId = perfilId,
                            empresaId = empresaId
                        )
                    } else {
                        // EDITAR
                        vm.updateUsuario(
                            id = editingUser!!.id,
                            nombre = nombre,
                            email = email,
                            password = password.ifBlank { null },
                            perfilId = perfilId,
                            empresaId = empresaId
                        )
                    }

                    // limpiar y salir de modo edición
                    editingUser = null
                    nombre = ""
                    email = ""
                    password = ""
                    selectedPerfilIndex = 0
                    selectedEmpresaIndex = 0
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = formEnabled
            ) {
                Text(if (editingUser == null) "Crear usuario" else "Guardar cambios")
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
                                // ---- botón EDITAR ----
                                TextButton(onClick = {
                                    editingUser = u
                                    nombre = u.nombre ?: ""
                                    email = u.email ?: ""
                                    password = ""

                                    // intentamos posicionar el perfil correcto
                                    selectedPerfilIndex =
                                        perfiles.indexOfFirst { it.codigo == u.perfil }
                                            .takeIf { it >= 0 } ?: 0
                                    // para empresa dejamos el índice actual (o 0)
                                }) {
                                    Text("Editar")
                                }

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
