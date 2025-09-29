package com.massiel.firmape.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun openFile(context: Context, absPath: String) {
    val file = File(absPath)
    if (!file.exists()) return
    val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val mime = when (file.extension.lowercase()) {
        "pdf" -> "application/pdf"
        "jpg","jpeg" -> "image/jpeg"
        "png" -> "image/png"
        else -> "*/*"
    }
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mime)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Abrir con"))
}

fun shareFile(context: Context, absPath: String) {
    val file = File(absPath)
    if (!file.exists()) return
    val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "*/*"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Compartir documento"))
}