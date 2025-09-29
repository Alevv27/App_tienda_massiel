package com.massiel.firmape.util


import android.content.Context
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Crea una copia del PDF con sufijo _firmado.pdf y dibuja un sello visible.
 * Devuelve la ruta absoluta del PDF firmado.
 */
fun stampPdfVisible(
    context: Context,
    srcAbsPath: String,
    signerName: String,
    reason: String? = null
): String {
    PDFBoxResourceLoader.init(context)

    val src = File(srcAbsPath)
    val ext = src.extension.ifBlank { "pdf" }
    val signed = File(src.parentFile, "${src.nameWithoutExtension}_firmado.$ext")

    PDDocument.load(src).use { doc ->
        // Tomamos la primera página (puedes recorrer todas si gustas)
        val page = doc.getPage(0)
        val mediaBox: PDRectangle = page.mediaBox

        // Área del sello (abajo a la derecha)
        val margin = 36f
        val sealWidth = 280f
        val sealHeight = 90f
        val x = mediaBox.width - sealWidth - margin
        val y = margin

        PDPageContentStream(doc, page, true, true).use { cs ->
            // Fondo del sello
            cs.addRect(x, y, sealWidth, sealHeight)
            cs.setNonStrokingColor(245, 245, 245) // gris claro
            cs.fill()

            // Borde
            cs.setStrokingColor(40, 80, 200) // azul
            cs.addRect(x, y, sealWidth, sealHeight)
            cs.stroke()

            // Texto
            cs.beginText()
            cs.setFont(PDType1Font.HELVETICA_BOLD, 11f)
            cs.setNonStrokingColor(20,20,20)
            cs.newLineAtOffset(x + 10f, y + sealHeight - 18f)
            cs.showText("FIRMADO ELECTRÓNICAMENTE")
            cs.endText()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val now = sdf.format(Date())

            fun line(text: String, offsetY: Float, small: Boolean = false) {
                cs.beginText()
                cs.setFont(if (small) PDType1Font.HELVETICA_OBLIQUE else PDType1Font.HELVETICA, if (small) 9f else 10f)
                cs.newLineAtOffset(x + 10f, y + offsetY)
                cs.showText(text)
                cs.endText()
            }

            line("Firmante: $signerName", sealHeight - 36f)
            line("Fecha: $now", sealHeight - 52f)
            if (!reason.isNullOrBlank()) {
                line("Motivo: $reason", sealHeight - 68f, small = true)
            }
        }

        doc.save(signed)
    }
    return signed.absolutePath
}