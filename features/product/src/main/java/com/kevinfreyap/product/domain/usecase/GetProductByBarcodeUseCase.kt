package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.InventoryBarcode
import com.kevinfreyap.domain.session.IActiveGroupSession
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetProductByBarcodeUseCase @Inject constructor (
    private val repository: IProductRepository,
    private val activeGroupSession: IActiveGroupSession,
) {
    suspend operator fun invoke(barcode: InventoryBarcode): Product? {
        if (barcode.value.isBlank()) return null

        val session = activeGroupSession.currentGroupId.first()
            ?: return null

        val groupId = GroupId(session)

        return repository.getProductByBarcode(barcode.value, groupId)
    }
}