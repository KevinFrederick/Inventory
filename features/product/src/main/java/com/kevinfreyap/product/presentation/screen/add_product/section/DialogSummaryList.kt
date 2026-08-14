package com.kevinfreyap.product.presentation.screen.add_product.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.error.BatchPriceError
import com.kevinfreyap.product.domain.model.error.BatchQuantityError
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.domain.model.error.ProductMinimumQuantityError
import com.kevinfreyap.product.presentation.state.AddBatchDetailState
import com.kevinfreyap.product.presentation.state.ProductFormDetailState
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun DialogSummaryList(
    title: String,
    subtitle: String,
    formErrors: ProductFormError?,
    productDetail: ProductFormDetailState,
    batchDetail: AddBatchDetailState?,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    positiveBtn: @Composable (() -> Unit)? = null,
    negativeBtn: @Composable (() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        DialogSummaryListContent(
            title = title,
            subtitle = subtitle,
            formErrors = formErrors,
            productDetail = productDetail,
            batchDetail = batchDetail,
            modifier = modifier,
            positiveBtn = positiveBtn,
            negativeBtn = negativeBtn
        )
    }
}

@Composable
fun DialogSummaryListContent(
    title: String,
    subtitle: String,
    formErrors: ProductFormError?,
    productDetail: ProductFormDetailState,
    batchDetail: AddBatchDetailState?,
    modifier: Modifier = Modifier,
    positiveBtn: @Composable (() -> Unit)? = null,
    negativeBtn: @Composable (() -> Unit)? = null
) {
    val hasBothButton = positiveBtn != null && negativeBtn != null
    val warnings = buildList {
        if (formErrors?.minQuantityError == ProductMinimumQuantityError.REQUIRES_CONFIRMATION) {
            add(stringResource(R.string.dialog_summary_list_min_quantity, productDetail.productMinQuantity))
        }
        batchDetail?.let {
            if (formErrors?.quantityError == BatchQuantityError.REQUIRES_CONFIRMATION) {
                add(stringResource(R.string.dialog_summary_list_quantity, batchDetail.batchQuantity))
            }
            if (formErrors?.priceError == BatchPriceError.REQUIRES_CONFIRMATION) {
                add(stringResource(R.string.dialog_summary_list_price, batchDetail.batchPrice))
            }
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 312.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Theme.custom.primaryText
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Theme.custom.secondaryText
            )

            Spacer(Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                warnings.forEach { warningText ->
                    Text(
                        text = warningText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Theme.custom.primaryText
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                horizontalArrangement = if (hasBothButton) Arrangement.spacedBy(8.dp) else Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (negativeBtn != null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = if (hasBothButton) Modifier.weight(1f) else Modifier
                    ) {
                        negativeBtn()
                    }
                }

                if (positiveBtn != null) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = if (hasBothButton) Modifier.weight(1f) else Modifier
                    ) {
                        positiveBtn()
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0x000000
)
@Composable
fun DialogSummaryListPreview() {
    InventoryTheme {
        DialogSummaryListContent(
            title = "Confirm Values",
            subtitle = "Please review these unusual values:",
            productDetail = ProductFormDetailState(
                productMinQuantity = "10000"
            ),
            batchDetail = AddBatchDetailState(
                batchQuantity = "11000",
                batchPrice = "1000000000"
            ),
            formErrors = ProductFormError(
                minQuantityError = ProductMinimumQuantityError.REQUIRES_CONFIRMATION,
                quantityError = BatchQuantityError.REQUIRES_CONFIRMATION,
                priceError = BatchPriceError.REQUIRES_CONFIRMATION
            ),
            positiveBtn = {
                AppPrimaryButton(
                    text = "Confirm All",
                    onClick = {  },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeBtn = {
                AppOutlinedButton(
                    text = "Edit",
                    onClick = {  },
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.hint,
                    modifier = Modifier.fillMaxWidth()
                )
            },

        )
    }
}