package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.InventoryBarcode
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.repository.IProductRepository
import javax.inject.Inject

class GetProductByBarcodeUseCase @Inject constructor (
    private val repository: IProductRepository
) {
    suspend operator fun invoke(barcode: InventoryBarcode): Product? {
        if (barcode.value.isBlank()) return null

        return repository.getProductByBarcode(barcode.value)
    }
}