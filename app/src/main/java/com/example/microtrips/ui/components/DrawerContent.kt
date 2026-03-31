package com.example.microtrips.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.microtrips.nav.Routes

data class DrawerDestination(
    val route: String,
    val title: String
)

private val destinations = listOf(
    DrawerDestination(Routes.Explore, "Explore"),
    DrawerDestination(Routes.Saved, "Saved"),
    DrawerDestination(Routes.Settings, "Settings")
)

@Composable
fun DrawerContent(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Micro Trips",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Text(
            text = "Small trips. Big vibes.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(Modifier.height(16.dp))

        destinations.forEach { destination ->
            NavigationDrawerItem(
                selected = currentRoute == destination.route,
                onClick = { onNavigate(destination.route) },
                label = { Text(destination.title) },
                icon = {
                    val icon = when (destination.route) {
                        Routes.Explore -> Icons.Default.Explore
                        Routes.Saved -> Icons.Default.Bookmarks
                        else -> Icons.Default.Settings
                    }
                    Icon(icon, contentDescription = null)
                },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}
