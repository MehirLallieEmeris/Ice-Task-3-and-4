package com.example.microtrips.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.microtrips.data.model.Gem
import com.example.microtrips.ui.components.GemCard

@Composable
fun ExploreScreen(
    gems: List<Gem>,
    categories: List<String>,
    provinces: List<String>,
    savedIds: Set<Int>,
    showBudgetBadges: Boolean,
    largeText: Boolean,
    onGemClick: (Int) -> Unit,
    onToggleSave: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedProvince by remember { mutableStateOf("All") }
    var maxBudget by remember { mutableFloatStateOf(2000f) }

    val filtered = gems.filter { gem ->
        val categoryMatch = selectedCategory == "All" || gem.category == selectedCategory
        val provinceMatch = selectedProvince == "All" || gem.province == selectedProvince
        val budgetMatch = gem.totalBudget <= maxBudget.toInt()
        val queryMatch = query.isBlank() ||
            gem.name.contains(query, ignoreCase = true) ||
            gem.shortDescription.contains(query, ignoreCase = true) ||
            gem.location.area.contains(query, ignoreCase = true)
        categoryMatch && provinceMatch && budgetMatch && queryMatch
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search spots...") },
            singleLine = true
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        ) {
            FilterDropdown(
                title = "Category",
                selected = selectedCategory,
                options = listOf("All") + categories,
                onOptionSelected = { selectedCategory = it },
                modifier = Modifier.weight(1f)
            )
            FilterDropdown(
                title = "Province",
                selected = selectedProvince,
                options = listOf("All") + provinces,
                onOptionSelected = { selectedProvince = it },
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Max budget: R${maxBudget.toInt()}",
            modifier = Modifier.padding(top = 12.dp)
        )
        Slider(
            value = maxBudget,
            onValueChange = { maxBudget = it },
            valueRange = 100f..2000f
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filtered, key = { it.id }) { gem ->
                GemCard(
                    gem = gem,
                    isSaved = gem.id in savedIds,
                    showBudgetBadge = showBudgetBadges,
                    largeText = largeText,
                    onClick = { onGemClick(gem.id) },
                    onToggleSave = { onToggleSave(gem.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterDropdown(
    title: String,
    selected: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            readOnly = true,
            value = selected,
            onValueChange = {},
            label = { Text(title) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            singleLine = true
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
