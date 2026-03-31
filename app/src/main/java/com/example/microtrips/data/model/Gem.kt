package com.example.microtrips.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Gem(
    val id: Int,
    val name: String,
    val province: String,
    val category: String,
    val image: String,
    val shortDescription: String,
    val timeNeeded: String,
    val bestSeason: String,
    val budget: Budget,
    val tips: List<String>,
    val location: Location,
    val tags: List<String>
) {
    val totalBudget: Int
        get() = budget.transport + budget.food + budget.entry + budget.misc
}

@Serializable
data class Budget(
    val transport: Int,
    val food: Int,
    val entry: Int,
    val misc: Int
)

@Serializable
data class Location(
    val area: String,
    val mapsQuery: String
)
