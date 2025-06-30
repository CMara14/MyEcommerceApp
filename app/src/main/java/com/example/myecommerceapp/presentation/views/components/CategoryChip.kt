package com.example.myecommerceapp.presentation.views.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.example.myecommerceapp.ui.theme.InputFieldColor
import com.example.myecommerceapp.ui.theme.LightGrayText
import com.example.myecommerceapp.ui.theme.PinkPastel
import com.example.myecommerceapp.ui.theme.White

@Composable
fun CategoryFilterChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        onClick = onClick,
        label = { Text(category) },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = InputFieldColor,
            labelColor = LightGrayText,
            selectedContainerColor = PinkPastel,
            selectedLabelColor = White,
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = LightGrayText,
            borderWidth = 1.dp,
            selectedBorderColor = PinkPastel,
            selectedBorderWidth = 1.dp,
        )
    )
}