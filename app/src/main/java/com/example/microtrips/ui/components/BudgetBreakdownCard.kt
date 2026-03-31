package com.example.microtrips.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.microtrips.data.model.Budget

@Composable
fun BudgetBreakdownCard(
    budget: Budget,
    modifier: Modifier = Modifier
) {
    val total = budget.transport + budget.food + budget.entry + budget.misc
    Card(modifier = modifier) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BreakdownRow("Transport", budget.transport)
            BreakdownRow("Food", budget.food)
            BreakdownRow("Entry", budget.entry)
            BreakdownRow("Misc", budget.misc)
            Spacer(Modifier.height(4.dp))
            BreakdownRow("Total", total, emphasized = true)
        }
    }
}

@Composable
private fun BreakdownRow(label: String, amount: Int, emphasized: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = label,
            style = if (emphasized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = "R$amount",
            style = if (emphasized) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal
        )
    }
}
