package com.example.microtrips.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.microtrips.prefs.AppPrefsState

@Composable
fun SettingsScreen(
    prefs: AppPrefsState,
    onDarkMode: (Boolean) -> Unit,
    onDynamicColor: (Boolean) -> Unit,
    onLargeText: (Boolean) -> Unit,
    onShowBudgetBadges: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        SettingsToggleRow("Dark mode", prefs.darkMode, onDarkMode)
        SettingsToggleRow("Dynamic color", prefs.dynamicColor, onDynamicColor)
        SettingsToggleRow("Large text", prefs.largeText, onLargeText)
        SettingsToggleRow("Show budget badges", prefs.showBudgetBadges, onShowBudgetBadges)
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}
