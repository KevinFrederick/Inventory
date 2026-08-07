package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

data class ProductFormError(
    val nameError: ProductNameError? = null,
    val categoryError: ProductCategoryError? = null,
    val descriptionError: ProductDescriptionError? = null,
    val barcodeError: ProductBarcodeError? = null,
    val skuError: ProductSkuError? = null,
    val imageError: ProductImageError? = null,
    val minQuantityError: ProductMinimumQuantityError? = null,
    val quantityError: BatchQuantityError? = null,
    val locationError: BatchLocationError? = null,
    val priceError: BatchPriceError? = null,
    val expirationError: BatchExpirationError? = null,
    val supplierError: BatchSupplierError? = null
): RootError
