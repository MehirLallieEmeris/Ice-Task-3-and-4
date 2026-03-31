package com.example.microtrips.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.microtrips.data.model.Gem
import com.example.microtrips.ui.components.BudgetBreakdownCard
import com.example.microtrips.ui.components.GemImage

@Composable
fun GemDetailsScreen(
    gem: Gem,
    isSaved: Boolean,
    largeText: Boolean,
    onToggleSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bodyStyle = if (largeText) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium

    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            GemImage(
                imageName = gem.image,
                contentDescription = gem.name,
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
        }
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.weight(1f)) {
                    Text(gem.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("${gem.location.area} · ${gem.province}", style = bodyStyle)
                }
                IconButton(onClick = onToggleSave) {
                    Icon(
                        imageVector = if (isSaved) Icons.Rounded.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Save trip"
                    )
                }
            }
        }
        item {
            Text(gem.shortDescription, style = bodyStyle)
        }
        item {
            Text("Budget breakdown", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            BudgetBreakdownCard(budget = gem.budget, modifier = Modifier.padding(top = 8.dp))
        }
        item {
            Text("Tips", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(top = 8.dp)) {
                gem.tips.forEach { tip ->
                    Text("• $tip", style = bodyStyle)
                }
            }
        }
        item {
            Button(
                onClick = {
                    val uri = Uri.parse("geo:0,0?q=${Uri.encode(gem.location.mapsQuery)}")
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open in Google Maps")
            }
        }
    }
}
