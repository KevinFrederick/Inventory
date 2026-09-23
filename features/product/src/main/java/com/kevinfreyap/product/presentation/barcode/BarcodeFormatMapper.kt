package com.kevinfreyap.product.presentation.barcode

import com.google.zxing.BarcodeFormat

fun mapStringToZxingFormat(formatString: String): BarcodeFormat {
    return when(formatString) {
        "EAN_13" -> BarcodeFormat.EAN_13
        "EAN_8" -> BarcodeFormat.EAN_8
        "UPC_A" -> BarcodeFormat.UPC_A
        "UPC_E" -> BarcodeFormat.UPC_E
        "CODE_128" -> BarcodeFormat.CODE_128
        "CODE_39" -> BarcodeFormat.CODE_39
        else -> BarcodeFormat.CODE_128
    }
}