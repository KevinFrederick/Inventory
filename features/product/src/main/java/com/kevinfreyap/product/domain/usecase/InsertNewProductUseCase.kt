package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.domain.manager.IImageManager
import com.kevinfreyap.product.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.repository.IProductRepository
import java.util.UUID
import javax.inject.Inject

class InsertNewProductUseCase @Inject constructor(
    private val repository: IProductRepository,
    private val imageManager: IImageManager,
    private val syncManager: ISyncManager,
    private val validateProductName: ValidateProductNameUseCase,
    private val validateProductCategory: ValidateProductCategoryUseCase,
    private val validateProductDescription: ValidateProductDescriptionUseCase,
    private val validateProductBarcode: ValidateProductBarcodeUseCase,
    private val validateProductSku: ValidateProductSkuUseCase,
    private val validateProductImage: ValidateProductImageUseCase,
    private val validateProductMinimumQuantity: ValidateProductMinimumQuantityUseCase,
    private val validateBatchQuantity: ValidateBatchQuantityUseCase,
    private val validateBatchLocation: ValidateBatchLocationUseCase,
    private val validateBatchPrice: ValidateBatchPriceUseCase,
    private val validateBatchExpiration: ValidateBatchExpirationUseCase,
    private val validateBatchSupplier: ValidateBatchSupplierUseCase,
    private val createOrGetCategory: CreateOrGetCategoryUseCase,
    private val createOrGetLocation: CreateOrGetLocationUseCase
) {
    suspend operator fun invoke(
        productName: String,
        productCategoryName: String,
        productDescription: String?,
        productBarcode: String?,
        productBarcodeFormat: String?,
        productSku: String?,
        productImageUriString: String?,
        productMinQuantity: String,
        isMinQuantityConfirmed: Boolean,
        addInitialStock: Boolean,
        batchQuantity: String,
        isQuantityConfirmed: Boolean,
        batchLocation: String,
        batchPrice: String,
        isPriceConfirmed: Boolean,
        batchExpiration: Long?,
        batchSupplier: String?

    ): Result<Unit, ProductFormError> {
        val nameResult = validateProductName(productName)
        val categoryResult = validateProductCategory(productCategoryName)
        val descriptionResult = validateProductDescription(productDescription)
        val barcodeResult = validateProductBarcode(productBarcode)
        val skuResult = validateProductSku(productSku)
        val imageResult = validateProductImage(productImageUriString)
        val minQuantityResult = validateProductMinimumQuantity(productMinQuantity, isMinQuantityConfirmed)

        val quantityResult = if (addInitialStock) {
            validateBatchQuantity(batchQuantity, isQuantityConfirmed)
        } else {
            // Pass success so doesn't error
            Result.Success(0)
        }

        val locationResult = if (addInitialStock) {
            validateBatchLocation(batchLocation)
        } else {
            null
        }

        val priceResult = if (addInitialStock) {
            validateBatchPrice(batchPrice, isPriceConfirmed)
        } else {
            Result.Success(0.0)
        }

        val expirationResult = if (addInitialStock) {
            validateBatchExpiration(batchExpiration)
        } else {
            Result.Success(null)
        }

        val supplierResult = if (addInitialStock) {
            validateBatchSupplier(batchSupplier)
        } else {
            Result.Success(null)
        }

        if (
            nameResult is Result.Error ||
            categoryResult is Result.Error ||
            descriptionResult is Result.Error ||
            barcodeResult is Result.Error ||
            skuResult is Result.Error ||
            imageResult is Result.Error ||
            minQuantityResult is Result.Error ||
            quantityResult is Result.Error ||
            locationResult is Result.Error ||
            priceResult is Result.Error ||
            expirationResult is Result.Error ||
            supplierResult is Result.Error
        ) {
            return Result.Error(
                ProductFormError(
                    nameError = nameResult.errorOrNull(),
                    categoryError = categoryResult.errorOrNull(),
                    descriptionError = descriptionResult.errorOrNull(),
                    barcodeError = barcodeResult.errorOrNull(),
                    skuError = skuResult.errorOrNull(),
                    imageError = imageResult.errorOrNull(),
                    minQuantityError = minQuantityResult.errorOrNull(),
                    quantityError = quantityResult.errorOrNull(),
                    locationError = locationResult?.errorOrNull(),
                    priceError = priceResult.errorOrNull(),
                    expirationError = expirationResult.errorOrNull(),
                    supplierError = supplierResult.errorOrNull()
                )
            )
        }

        val validCategoryName = categoryResult.getOrNull()!!
        val categoryDomain = createOrGetCategory(validCategoryName)

        val newProductId = ProductId("product-${UUID.randomUUID()}")

        // use `?:` to satisfy the Kotlin compiler's null-safety.
        // never be null here because the error-check block above caught all errors, guaranteeing these are Result.Success.
        val validBatchQuantity = quantityResult.getOrNull() ?: 0
        val validPrice = priceResult.getOrNull() ?: 0.0

        val initialBatch = if (addInitialStock && validBatchQuantity > 0) {
            // 1st `!!` : locationResult exists because addInitialStock is true (bypass didn't happen).
            // 2nd `!!` : The Location data exists because the error-check above guaranteed it's a Success.
            val validLocation = locationResult!!.getOrNull()!!
            val locationDomain = createOrGetLocation(validLocation)

            listOf(
                StockBatch(
                    batchId = BatchId("batch-${UUID.randomUUID()}"),
                    productId = newProductId,
                    location = locationDomain,
                    quantity = validBatchQuantity,
                    price = validPrice,
                    expirationDate = expirationResult.getOrNull(),
                    supplier = supplierResult.getOrNull(),
                    createdAt = System.currentTimeMillis(),
                    lastUpdated = System.currentTimeMillis()
                )
            )
        } else {
            emptyList()
        }

        val currentUri = imageResult.getOrNull()

        val permanentPath = if (currentUri != null && currentUri.startsWith("content://")) {
            imageManager.saveImageToInternalStorage(currentUri)
        } else {
            currentUri
        }

        repository.insertProduct(
            Product(
                productId = newProductId,
                category = categoryDomain,
                name = nameResult.getOrNull()!!,
                description = descriptionResult.getOrNull(),
                barcode = barcodeResult.getOrNull(),
                barcodeFormat = productBarcodeFormat,
                sku = skuResult.getOrNull(),
                localImagePath = permanentPath,
                remoteImageUrl = null,
                minimumQuantity = minQuantityResult.getOrNull() ?: 0,
                batches = initialBatch,
                createdAt = System.currentTimeMillis(),
                lastUpdated = System.currentTimeMillis()
            )
        )

        syncManager.triggerSync()
        return Result.Success(Unit)
    }
}