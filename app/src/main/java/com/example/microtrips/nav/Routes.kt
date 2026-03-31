package com.example.microtrips.nav

object Routes {
    const val Explore = "explore"
    const val Saved = "saved"
    const val Settings = "settings"
    const val Details = "details/{gemId}"

    fun details(gemId: Int): String = "details/$gemId"
}
