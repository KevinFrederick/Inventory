package com.kevinfreyap.product.presentation.barcode

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.MultiFormatWriter
import androidx.core.graphics.createBitmap
import com.google.zxing.WriterException
import com.kevinfreyap.domain.model.InventoryBarcode

fun generateBarcodeBitmap(barcode: InventoryBarcode?): Bitmap? {
    if (barcode == null || barcode.value.isBlank()) return null

    return try {
        val bitMatrix = MultiFormatWriter().encode(
            barcode.value,
            mapStringToZxingFormat(barcode.format),
            1024,
            256
        )

        val width = bitMatrix.width
        val height = bitMatrix.height

        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
            }
        }

        val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }
}