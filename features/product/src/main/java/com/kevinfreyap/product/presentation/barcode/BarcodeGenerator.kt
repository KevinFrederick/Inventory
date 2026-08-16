package com.kevinfreyap.product.presentation.barcode

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.WriterException

fun generateBarcodeBitmap(barcodeText: String?): Bitmap? {
    if (barcodeText.isNullOrBlank()) return null

    return try {
        val bitMatrix = MultiFormatWriter().encode(
            barcodeText,
            BarcodeFormat.CODE_128,
            800,
            200
        )

        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap[x, y] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }
}