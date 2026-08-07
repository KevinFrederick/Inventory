package com.kevinfreyap.product.presentation.screen.bottom_sheet.filter.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.components.CheckboxSelectionItem
import com.kevinfreyap.product.presentation.model.CategoryUi
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionCategory(
    categories: List<CategoryUi>,
    selectedCategories: Set<CategoryUi>,
    modifier: Modifier = Modifier,
    onCategoryToggled: (CategoryUi) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.label_category),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Theme.custom.primaryText,
        )

        Spacer(Modifier.height(8.dp))

        if(categories.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = stringResource(R.string.warning_no_category_yet),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Theme.custom.hint
                )
            }

            Spacer(Modifier.height(8.dp))
        } else {
            FlowRow(
                maxItemsInEachRow = 2,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                categories.forEach { categoryUi ->
                    CheckboxSelectionItem(
                        checkboxLabel = categoryUi.name,
                        onClick = { onCategoryToggled(categoryUi) },
                        isSelected = (categoryUi in selectedCategories),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionCategoryPreview() {
    var selectedCategories by remember { mutableStateOf(emptySet<CategoryUi>()) }

    InventoryTheme {
        SectionCategory(
//            categories = emptyList(),
            categories = listOf(
                CategoryUi(
                    id = "Cat_01",
                    name = "Electronic"
                ),
                CategoryUi(
                    id = "Cat_02",
                    name = "Food"
                ),
                CategoryUi(
                    id = "Cat_03",
                    name = "Furniture"
                )
            ),
            selectedCategories = selectedCategories,
            onCategoryToggled = { categoryUi ->
                selectedCategories = if (categoryUi in selectedCategories) {
                    selectedCategories - categoryUi
                } else {
                    selectedCategories + categoryUi
                }
            }
        )
    }
}