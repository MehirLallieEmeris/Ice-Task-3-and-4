package com.example.microtrips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.microtrips.data.repository.GemsRepository
import com.example.microtrips.nav.Routes
import com.example.microtrips.prefs.AppPrefs
import com.example.microtrips.prefs.AppPrefsState
import com.example.microtrips.ui.components.DrawerContent
import com.example.microtrips.ui.screens.ExploreScreen
import com.example.microtrips.ui.screens.GemDetailsScreen
import com.example.microtrips.ui.screens.SavedScreen
import com.example.microtrips.ui.screens.SettingsScreen
import com.example.microtrips.ui.theme.MicroTripsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val gemsRepository = GemsRepository(applicationContext)
        val appPrefs = AppPrefs(applicationContext)

        setContent {
            val prefs by appPrefs.state.collectAsState(initial = AppPrefsState())
            MicroTripsTheme(
                darkTheme = prefs.darkMode,
                dynamicColor = prefs.dynamicColor
            ) {
                MicroTripsApp(
                    gemsRepository = gemsRepository,
                    appPrefs = appPrefs,
                    prefs = prefs
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MicroTripsApp(
    gemsRepository: GemsRepository,
    appPrefs: AppPrefs,
    prefs: AppPrefsState
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val gems by gemsRepository.gems.collectAsState()
    val categories by gemsRepository.categories.collectAsState()
    val provinces by gemsRepository.provinces.collectAsState()

    LaunchedEffect(Unit) { gemsRepository.load() }

    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: Routes.Explore
    val onDetails = currentRoute.startsWith("details/")
    val title = when {
        onDetails -> "Micro Trips"
        currentRoute == Routes.Saved -> "Saved"
        currentRoute == Routes.Settings -> "Settings"
        else -> "Micro Trips"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !onDetails,
        drawerContent = {
            DrawerContent(currentRoute = currentRoute) { route ->
                scope.launch { drawerState.close() }
                navController.navigate(route) {
                    launchSingleTop = true
                    popUpTo(Routes.Explore) { saveState = true }
                    restoreState = true
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (onDetails) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        } else {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.Explore,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Routes.Explore) {
                    ExploreScreen(
                        gems = gems,
                        categories = categories,
                        provinces = provinces,
                        savedIds = prefs.savedGemIds,
                        showBudgetBadges = prefs.showBudgetBadges,
                        largeText = prefs.largeText,
                        onGemClick = { navController.navigate(Routes.details(it)) },
                        onToggleSave = { id -> scope.launch { appPrefs.toggleSavedGem(id) } }
                    )
                }
                composable(Routes.Saved) {
                    SavedScreen(
                        gems = gems,
                        savedIds = prefs.savedGemIds,
                        showBudgetBadges = prefs.showBudgetBadges,
                        largeText = prefs.largeText,
                        onGemClick = { navController.navigate(Routes.details(it)) },
                        onToggleSave = { id -> scope.launch { appPrefs.toggleSavedGem(id) } }
                    )
                }
                composable(Routes.Settings) {
                    SettingsScreen(
                        prefs = prefs,
                        onDarkMode = { value -> scope.launch { appPrefs.setDarkMode(value) } },
                        onDynamicColor = { value -> scope.launch { appPrefs.setDynamicColor(value) } },
                        onLargeText = { value -> scope.launch { appPrefs.setLargeText(value) } },
                        onShowBudgetBadges = { value -> scope.launch { appPrefs.setShowBudgetBadges(value) } }
                    )
                }
                composable(
                    route = Routes.Details,
                    arguments = listOf(navArgument("gemId") { type = NavType.IntType })
                ) { entry ->
                    val gemId = entry.arguments?.getInt("gemId") ?: -1
                    val gem = gems.firstOrNull { it.id == gemId }
                    if (gem == null) {
                        Text("Trip not found")
                    } else {
                        GemDetailsScreen(
                            gem = gem,
                            isSaved = gem.id in prefs.savedGemIds,
                            largeText = prefs.largeText,
                            onToggleSave = { scope.launch { appPrefs.toggleSavedGem(gem.id) } }
                        )
                    }
                }
            }
        }
    }
}
