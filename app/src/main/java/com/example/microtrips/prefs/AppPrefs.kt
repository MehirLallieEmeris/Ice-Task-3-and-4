package com.example.microtrips.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "micro_trips_prefs")

data class AppPrefsState(
    val darkMode: Boolean = true,
    val dynamicColor: Boolean = true,
    val largeText: Boolean = false,
    val showBudgetBadges: Boolean = true,
    val savedGemIds: Set<Int> = emptySet()
)

class AppPrefs(private val context: Context) {
    private val darkModeKey = booleanPreferencesKey("dark_mode")
    private val dynamicColorKey = booleanPreferencesKey("dynamic_color")
    private val largeTextKey = booleanPreferencesKey("large_text")
    private val budgetBadgesKey = booleanPreferencesKey("budget_badges")
    private val savedIdsKey = stringSetPreferencesKey("saved_ids")

    val state: Flow<AppPrefsState> = context.dataStore.data.map { prefs ->
        AppPrefsState(
            darkMode = prefs[darkModeKey] ?: true,
            dynamicColor = prefs[dynamicColorKey] ?: true,
            largeText = prefs[largeTextKey] ?: false,
            showBudgetBadges = prefs[budgetBadgesKey] ?: true,
            savedGemIds = prefs[savedIdsKey]?.mapNotNull { it.toIntOrNull() }?.toSet().orEmpty()
        )
    }

    suspend fun setDarkMode(value: Boolean) = context.dataStore.edit { it[darkModeKey] = value }
    suspend fun setDynamicColor(value: Boolean) = context.dataStore.edit { it[dynamicColorKey] = value }
    suspend fun setLargeText(value: Boolean) = context.dataStore.edit { it[largeTextKey] = value }
    suspend fun setShowBudgetBadges(value: Boolean) = context.dataStore.edit { it[budgetBadgesKey] = value }

    suspend fun toggleSavedGem(id: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[savedIdsKey].orEmpty().toMutableSet()
            val textId = id.toString()
            if (current.contains(textId)) current.remove(textId) else current.add(textId)
            prefs[savedIdsKey] = current
        }
    }
}
