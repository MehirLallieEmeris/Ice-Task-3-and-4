package com.example.microtrips.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

private val bundledImages = setOf(
    "umdloti_tidal_pool.svg",
    "hennops_hike.svg",
    "silvermine_dam.svg",
    "karkloof_falls.svg",
    "clarens_golden_hour.svg",
    "blyde_river_view.svg",
    "cintsa_beach.svg",
    "neethlingshof_picnic.svg",
    "maboneng_street_art.svg",
    "hogsback_forest.svg"
)

@Composable
fun GemImage(
    imageName: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components { add(SvgDecoder.Factory()) }
            .build()
    }
    val resolved = resolveAssetImage(imageName)
    if (resolved == null) {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contentDescription.take(1),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        return
    }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data("file:///android_asset/images/$resolved")
            .crossfade(true)
            .build(),
        imageLoader = imageLoader,
        contentDescription = contentDescription,
        modifier = modifier
    )
}

private fun resolveAssetImage(name: String): String? {
    val direct = name.substringAfterLast('/')
    if (direct in bundledImages) return direct
    val svgCandidate = direct.replaceAfterLast('.', "svg")
    if (svgCandidate in bundledImages) return svgCandidate
    return null
}
