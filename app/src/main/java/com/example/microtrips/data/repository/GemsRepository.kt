package com.example.microtrips.data.repository

import android.content.Context
import com.example.microtrips.data.model.Gem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

class GemsRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    private val _gems = MutableStateFlow<List<Gem>>(emptyList())
    val gems: StateFlow<List<Gem>> = _gems.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _provinces = MutableStateFlow<List<String>>(emptyList())
    val provinces: StateFlow<List<String>> = _provinces.asStateFlow()

    suspend fun load() {
        _gems.value = readList("data/gems.json", ListSerializer(Gem.serializer()))
        _categories.value = readList("data/categories.json", ListSerializer(String.serializer()))
        _provinces.value = readList("data/provinces.json", ListSerializer(String.serializer()))
    }

    private fun <T> readList(path: String, serializer: KSerializer<List<T>>): List<T> {
        return context.assets.open(path).bufferedReader().use { reader ->
            json.decodeFromString(serializer, reader.readText())
        }
    }
}
