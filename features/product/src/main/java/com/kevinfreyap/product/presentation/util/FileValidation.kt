package com.kevinfreyap.product.presentation.util

import com.kevinfreyap.product.domain.model.Product
import java.io.File

fun Product.resolveDisplayedImage(): String? {
    return if (this.localImagePath != null && File(this.localImagePath).exists()) {
        this.localImagePath
    } else {
        this.remoteImageUrl?.let { "$it?v=${this.lastUpdated}" }
    }
}