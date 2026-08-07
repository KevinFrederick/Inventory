package com.kevinfreyap.product.presentation.action

sealed interface AddProductAction {
    // Product Detail Actions
    sealed interface ProductDetailAction: AddProductAction {
        data class OnImageUriChanged(val uri: String?): ProductDetailAction
        data class OnNameChanged(val name: String): ProductDetailAction
        data class OnCategoryChanged(val category: String): ProductDetailAction
        data class OnCreateNewCategory(val newCategory: String): ProductDetailAction
        data class OnDescriptionChanged(val desc: String?): ProductDetailAction
        data class OnMinQuantityChanged(val qty: String): ProductDetailAction
    }

    // Product Image Actions
    sealed interface ImagePickerAction : AddProductAction {
        data object OnUploadClick : ImagePickerAction
        data object OnDismissSheet : ImagePickerAction
        data object OnGalleryClick : ImagePickerAction
        data object OnCameraClick : ImagePickerAction
        data object OnRemoveImage: ImagePickerAction
        data object OnDismissRemoveDialog: ImagePickerAction
        data object OnConfirmRemoveDialog: ImagePickerAction
    }

    // Product Identification Actions
    sealed interface ProductIdentificationAction: AddProductAction {
        data class OnBarcodeChanged(val barcode: String?): ProductIdentificationAction
        data class OnSkuChanged(val sku: String?): ProductIdentificationAction
    }

    // Batch Quantity & Price Actions
    sealed interface BatchDetailAction: AddProductAction {
        data class OnAddInitialStockToggled(val isChecked: Boolean): BatchDetailAction
        data class OnBatchQuantityChanged(val qty: String): BatchDetailAction
        data object OnQuantityIncremented: BatchDetailAction
        data object OnQuantityDecremented: BatchDetailAction
        data class OnBatchPriceChanged(val price: String): BatchDetailAction
        data class OnBatchLocationChanged(val loc: String): BatchDetailAction
        data class OnCreateNewLocation(val newLocName: String): BatchDetailAction
    }

    // Batch Logistic Actions
    sealed interface BatchInformationAction: AddProductAction {
        data class OnBatchSupplierChanged(val supplier: String?): BatchInformationAction
        data class OnBatchExpirationChanged(val expString: String): BatchInformationAction
        data object OnOpenExpirationDialog: BatchInformationAction
        data object OnBatchExpirationConfirm: BatchInformationAction
    }

    sealed interface SummaryDialogAction: AddProductAction {
        data object OnConfirmAllWarnings: SummaryDialogAction
        data object OnDismissWarningsDialog: SummaryDialogAction
    }

    sealed interface StatusDialogAction: AddProductAction {
        data object OnDismissError: StatusDialogAction
    }

    // Submit
    data object SaveProduct: AddProductAction
    data object ResetForm: AddProductAction
}