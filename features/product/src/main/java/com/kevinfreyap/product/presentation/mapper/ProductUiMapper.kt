package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.presentation.model.ProductListItemUi
import com.kevinfreyap.product.presentation.util.StockLevelCalculator.calculateStockLevel

fun Product.toUiModel(): ProductListItemUi {
    return ProductListItemUi(
        id = this.productId.value,
        name = this.name,
        quantity = this.quantity,
        stockLevel = calculateStockLevel(qty = this.quantity, minQty = this.minimumQuantity),
        category = this.category.name,
        imageUri = this.imageUri,
        sku = this.sku
    )
}