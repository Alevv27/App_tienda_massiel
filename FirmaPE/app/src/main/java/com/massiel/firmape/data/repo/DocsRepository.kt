package com.massiel.firmape.data.repo

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.massiel.firmape.data.model.Documento
import com.massiel.firmape.util.stampPdfVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.atomic.AtomicLong

class DocsRepository(private val ctx: Context) {

    private val gson = Gson()
    private val dataFile: File by lazy {
        File(ctx.filesDir, "documentos/documentos.json").apply { parentFile?.mkdirs() }
    }
    // contador simple de ids (carga el último usado desde archivo)
    private val idCounter = AtomicLong(loadMaxId())

    suspend fun listar(estado: String?): List<Documento> = withContext(Dispatchers.IO) {
        val all = loadAll()
        if (estado.isNullOrBlank()) all.sortedByDescending { it.fechaRegistro }
        else all.filter { it.estado == estado }.sortedByDescending { it.fechaRegistro }
    }

    suspend fun subirDesdeUri(uri: Uri, titulo: String, tipo: String): Long =
        withContext(Dispatchers.IO) {
            val cr: ContentResolver = ctx.contentResolver
            val name = queryDisplayName(cr, uri) ?: "documento"
            val size = querySize(cr, uri) ?: 0L

            val target = File(ctx.filesDir, "docs/${System.currentTimeMillis()}_$name")
                .apply { parentFile?.mkdirs() }

            cr.openInputStream(uri).use { input ->
                target.outputStream().use { out -> input?.copyTo(out) }
            }

            val id = idCounter.incrementAndGet()
            val doc = Documento(
                id = id,
                titulo = if (titulo.isBlank()) name.substringBeforeLast('.') else titulo,
                tipo = tipo,
                sizeBytes = size,
                estado = "PENDIENTE",
                path = target.absolutePath,
                fileName = name,
                fechaRegistro = System.currentTimeMillis()
            )

            val all = loadAll().toMutableList()
            all.add(doc)
            saveAll(all)
            id
        }

    suspend fun cambiarEstado(id: Long, nuevo: String, signerName: String) = withContext(Dispatchers.IO) {
        val all = loadAll().toMutableList()
        val idx = all.indexOfFirst { it.id == id }
        if (idx < 0) return@withContext

        val original = all[idx]
        var updated = original.copy(estado = nuevo)

        if (nuevo == "FIRMADO") {
            // crea copia _firmado.pdf con sello visible
            val signedPath = stampPdfVisible(ctx, original.path, signerName = signerName, reason = "Aprobación de documento")
            val signedFile = File(signedPath)
            updated = updated.copy(path = signedFile.absolutePath, fileName = signedFile.name)
        }

        all[idx] = updated
        saveAll(all)
    }


    suspend fun eliminar(id: Long) = withContext(Dispatchers.IO) {
        val all = loadAll().toMutableList()
        val idx = all.indexOfFirst { it.id == id }
        if (idx >= 0) {
            // borra también el archivo físico
            runCatching { File(all[idx].path).takeIf { it.exists() }?.delete() }
            all.removeAt(idx)
            saveAll(all)
        }
    }

    // --------- helpers ----------
    private fun loadAll(): List<Documento> {
        if (!dataFile.exists()) return emptyList()
        return runCatching {
            val json = dataFile.readText()
            val type = object : TypeToken<List<Documento>>() {}.type
            gson.fromJson<List<Documento>>(json, type) ?: emptyList()
        }.getOrElse { emptyList() }
    }

    private fun saveAll(list: List<Documento>) {
        dataFile.writeText(gson.toJson(list))
    }

    private fun loadMaxId(): Long = try {
        loadAll().maxOfOrNull { it.id } ?: 0L
    } catch (_: Exception) { 0L }

    private fun queryDisplayName(cr: ContentResolver, uri: Uri): String? =
        cr.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)
            ?.use { c -> if (c.moveToFirst()) c.getString(0) else null }

    private fun querySize(cr: ContentResolver, uri: Uri): Long? =
        cr.query(uri, arrayOf(android.provider.OpenableColumns.SIZE), null, null, null)
            ?.use { c -> if (c.moveToFirst()) c.getLong(0) else null }
}