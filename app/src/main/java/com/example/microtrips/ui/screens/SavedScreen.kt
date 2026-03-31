package com.example.microtrips.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.microtrips.data.model.Gem
import com.example.microtrips.ui.components.GemCard

@Composable
fun SavedScreen(
    gems: List<Gem>,
    savedIds: Set<Int>,
    showBudgetBadges: Boolean,
    largeText: Boolean,
    onGemClick: (Int) -> Unit,
    onToggleSave: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val savedGems = gems.filter { it.id in savedIds }
    if (savedGems.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No saved trips yet.")
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(savedGems, key = { it.id }) { gem ->
            GemCard(
                gem = gem,
                isSaved = true,
                showBudgetBadge = showBudgetBadges,
                largeText = largeText,
                onClick = { onGemClick(gem.id) },
                onToggleSave = { onToggleSave(gem.id) }
            )
        }
    }
}
