package com.kevinfreyap.product.presentation.screen.filter.section

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.model.LocationUi
import com.kevinfreyap.ui.components.AppDropdownField
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionLocation(
    locationList: List<LocationUi>,
    selectedLocation: LocationUi?,
    onSelectLocation: (LocationUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.label_location),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Theme.custom.primaryText,
        )

        Spacer(Modifier.height(8.dp))

        AppDropdownField(
            value = selectedLocation?.name ?: "",
            label = stringResource(R.string.placeholder_select_location),
            options = locationList,
            unfocusedColor = Theme.custom.primaryText,
            floatingLabel = false,
            optionText = { locationUi -> locationUi.name },
            onSearchQueryChange = {},
            onOptionSelected = onSelectLocation,
            modifier = Modifier,
            readOnly = true,
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionLocationPreview() {
    InventoryTheme {
        SectionLocation(
            locationList = emptyList(),
            selectedLocation = LocationUi(
                name = "",
                id = ""
            ),
            onSelectLocation = {}
        )
    }
}