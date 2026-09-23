package com.kevinfreyap.scanner.barcode

import com.google.mlkit.vision.barcode.common.Barcode

fun getBarcodeFormatString(mlKitFormat: Int): String {
    return when(mlKitFormat) {
        Barcode.FORMAT_EAN_13 -> "EAN_13"
        Barcode.FORMAT_EAN_8 -> "EAN_8"
        Barcode.FORMAT_UPC_A -> "UPC_A"
        Barcode.FORMAT_UPC_E -> "UPC_E"
        Barcode.FORMAT_CODE_128 -> "CODE_128"
        Barcode.FORMAT_CODE_39 -> "CODE_39"
        else -> "CODE_128"
    }
}