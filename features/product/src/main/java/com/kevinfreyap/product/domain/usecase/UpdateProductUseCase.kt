package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.error.DatabaseError
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.domain.repository.IImageManager
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import kotlin.text.startsWith

class UpdateProductUseCase @Inject constructor(
    private val repository: IProductRepository,
    private val imageManager: IImageManager,
    private val validateProductName: ValidateProductNameUseCase,
    private val validateProductCategory: ValidateProductCategoryUseCase,
    private val validateProductDescription: ValidateProductDescriptionUseCase,
    private val validateProductBarcode: ValidateProductBarcodeUseCase,
    private val validateProductSku: ValidateProductSkuUseCase,
    private val validateProductImage: ValidateProductImageUseCase,
    private val validateProductMinimumQuantity: ValidateProductMinimumQuantityUseCase,
    private val createOrGetCategory: CreateOrGetCategoryUseCase,
) {
    suspend operator fun invoke(
        productId: ProductId,
        productName: String,
        productCategoryName: String,
        productDescription: String?,
        productBarcode: String?,
        productSku: String?,
        productImageUriString: String?,
        productMinQuantity: String,
        isMinQuantityConfirmed: Boolean,
    ): Result<Unit, ProductFormError> {
        val nameResult = validateProductName(productName)
        val categoryResult = validateProductCategory(productCategoryName)
        val descriptionResult = validateProductDescription(productDescription)
        val barcodeResult = validateProductBarcode(productBarcode)
        val skuResult = validateProductSku(productSku)
        val imageResult = validateProductImage(productImageUriString)
        val minQuantityResult = validateProductMinimumQuantity(productMinQuantity, isMinQuantityConfirmed)

        if (
            nameResult is Result.Error ||
            categoryResult is Result.Error ||
            descriptionResult is Result.Error ||
            barcodeResult is Result.Error ||
            skuResult is Result.Error ||
            imageResult is Result.Error ||
            minQuantityResult is Result.Error
        ) {
            return Result.Error(
                ProductFormError(
                    nameError = nameResult.errorOrNull(),
                    categoryError = categoryResult.errorOrNull(),
                    descriptionError = descriptionResult.errorOrNull(),
                    barcodeError = barcodeResult.errorOrNull(),
                    skuError = skuResult.errorOrNull(),
                    imageError = imageResult.errorOrNull(),
                    minQuantityError = minQuantityResult.errorOrNull()
                )
            )
        }

        val validCategoryName = categoryResult.getOrNull()!!
        val categoryDomain = createOrGetCategory(validCategoryName)

        val existingProduct = repository.getProductById(productId).firstOrNull()
            ?: return Result.Error(
                ProductFormError(
                    databaseError = DatabaseError.NOT_FOUND
                )
            )

        val currentUri = imageResult.getOrNull()

        val permanentPath = if (currentUri != null && currentUri.startsWith("content://")) {
            imageManager.saveImageToInternalStorage(currentUri)
        } else {
            currentUri
        }

        if (existingProduct.imageUri != null && existingProduct.imageUri != permanentPath) {
            imageManager.deleteImage(existingProduct.imageUri)
        }

        repository.updateProduct(
            Product(
                productId = productId,
                category = categoryDomain,
                name = nameResult.getOrNull()!!,
                description = descriptionResult.getOrNull(),
                barcode = barcodeResult.getOrNull(),
                sku = skuResult.getOrNull(),
                imageUri = permanentPath,
                minimumQuantity = minQuantityResult.getOrNull() ?: 0,
                batches = existingProduct.batches,
                createdAt = existingProduct.createdAt,
                lastUpdated = System.currentTimeMillis(),
            )
        )

        return Result.Success(Unit)
    }
}