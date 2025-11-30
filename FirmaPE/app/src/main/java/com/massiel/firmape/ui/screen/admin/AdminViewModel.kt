package com.massiel.firmape.ui.screen.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massiel.firmape.data.model.Usuario
import com.massiel.firmape.data.model.Empresa
import com.massiel.firmape.data.model.dto.Perfil
import com.massiel.firmape.data.model.dto.UsuarioCreateRequest
import com.massiel.firmape.data.model.dto.UsuarioUpdateRequest
import com.massiel.firmape.data.remote.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AdminViewModel : ViewModel() {

    var usuarios by mutableStateOf<List<Usuario>>(emptyList())
        private set

    var perfiles by mutableStateOf<List<Perfil>>(emptyList())
        private set

    var empresas by mutableStateOf<List<Empresa>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    var success by mutableStateOf<String?>(null)
        private set

    private val api = RetrofitClient.api()

    // ==================== CARGA INICIAL ====================
    fun loadAll() {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                success = null

                val uResp = api.getUsuarios()
                val pResp = api.getPerfiles()
                val eResp = api.getEmpresas()

                // Estas respuestas SÍ tienen ok + lista
                if (uResp.ok) usuarios = uResp.usuarios
                if (pResp.ok) perfiles = pResp.perfiles
                if (eResp.ok) empresas = eResp.empresas

            } catch (e: Exception) {
                error = "Error al cargar datos: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    // ==================== CREAR USUARIO ====================
    fun createUsuario(
        nombre: String,
        email: String,
        password: String,
        perfilId: Int,
        empresaId: Int
    ) {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                success = null

                val body = UsuarioCreateRequest(
                    nombre = nombre,
                    email = email,
                    password = password,
                    perfil_id = perfilId,
                    empresa_id = empresaId
                )

                // En tu ApiService, esto devuelve Usuario, no un wrapper
                api.createUsuario(body)

                // Mensaje de éxito
                success = "Usuario creado"

                // Recarga toda la data para que el nuevo usuario
                // aparezca inmediatamente con todos sus datos
                loadAll()

            } catch (e: Exception) {
                error = "Error al crear usuario: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    // ==================== ACTIVAR / DESACTIVAR ====================
    fun toggleActivo(u: Usuario) {
        val current = u.activo ?: true
        viewModelScope.launch {
            try {
                loading = true
                error = null
                success = null

                val body = UsuarioUpdateRequest(activo = !current)

                api.updateUsuario(u.id, body)

                success = if (!current) "Usuario activado" else "Usuario desactivado"

                // Refrescamos lista desde backend
                loadAll()

            } catch (e: Exception) {
                error = "Error al actualizar usuario: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    // ==================== ELIMINAR USUARIO ====================
    fun deleteUsuario(id: Long) {
        viewModelScope.launch {
            try {
                loading = true
                error = null
                success = null

                api.deleteUsuario(id)

                success = "Usuario eliminado"

                // Refrescamos lista completa
                loadAll()

            } catch (e: Exception) {
                error = "Error al eliminar usuario: ${e.message}"
            } finally {
                loading = false
            }
        }
    }
}
