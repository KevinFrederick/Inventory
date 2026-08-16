package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.error.DatabaseError
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import java.util.UUID
import javax.inject.Inject

class SaveBatchUseCase @Inject constructor(
    private val repository: IStockBatchRepository,
    private val validateBatchQuantity: ValidateBatchQuantityUseCase,
    private val validateBatchLocation: ValidateBatchLocationUseCase,
    private val validateBatchPrice: ValidateBatchPriceUseCase,
    private val validateBatchExpiration: ValidateBatchExpirationUseCase,
    private val validateBatchSupplier: ValidateBatchSupplierUseCase,
    private val createOrGetLocation: CreateOrGetLocationUseCase
) {
    suspend operator fun invoke(
        productId: ProductId,
        batchId: BatchId?,
        batchQuantity: String,
        isQuantityConfirmed: Boolean,
        batchLocation: String,
        batchPrice: String,
        isPriceConfirmed: Boolean,
        batchExpiration: Long?,
        batchSupplier: String?
    ): Result<Unit, ProductFormError> {
        val quantityResult = validateBatchQuantity(batchQuantity, isQuantityConfirmed)
        val locationResult = validateBatchLocation(batchLocation)
        val priceResult = validateBatchPrice(batchPrice, isPriceConfirmed)
        val expirationResult = validateBatchExpiration(batchExpiration)
        val supplierResult = validateBatchSupplier(batchSupplier)

        if (
            quantityResult is Result.Error ||
            locationResult is Result.Error ||
            priceResult is Result.Error ||
            expirationResult is Result.Error ||
            supplierResult is Result.Error
        ) {
            return Result.Error(
                ProductFormError(
                    quantityError = quantityResult.errorOrNull(),
                    locationError = locationResult.errorOrNull(),
                    priceError = priceResult.errorOrNull(),
                    expirationError = expirationResult.errorOrNull(),
                    supplierError = supplierResult.errorOrNull()
                )
            )
        }

        val validBatchQuantity = quantityResult.getOrNull() ?: 0
        val validPrice = priceResult.getOrNull() ?: 0.0

        val validLocation = locationResult.getOrNull()!!
        val locationDomain = createOrGetLocation(validLocation)

        val batch = StockBatch(
            batchId = batchId ?: BatchId("batch-${UUID.randomUUID()}"),
            productId = productId,
            location = locationDomain,
            quantity = validBatchQuantity,
            price = validPrice,
            expirationDate = expirationResult.getOrNull(),
            supplier = supplierResult.getOrNull(),
            lastUpdated = System.currentTimeMillis()
        )

        val timestamp = System.currentTimeMillis()

        if (batchId == null) {
            repository.insertBatchToProduct(
                stockBatch = batch,
                timestamp = timestamp
            )
        } else {
            val rowUpdated = repository.updateBatch(
                stockBatch = batch,
                timestamp = timestamp
            )

            if (rowUpdated == 0) {
                return Result.Error(
                    ProductFormError(
                        databaseError = DatabaseError.NOT_FOUND
                    )
                )
            }
        }

        return Result.Success(Unit)
    }
}