package com.dhruva.txtvoice.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.dhruva.txtvoice.ui.history.HistoryScreen
import com.dhruva.txtvoice.ui.homeTranscribe.HomeTranscribeScreen
import com.dhruva.txtvoice.ui.settings.SettingsScreen
import com.dhruva.txtvoice.ui.speak.SpeakScreen
import com.dhruva.txtvoice.ui.theme.DarkBackground
import com.dhruva.txtvoice.ui.theme.DarkSurfaceVariant
import kotlinx.serialization.serializer

@Composable
fun rememberMyAppNavBackStack(vararg elements: TxtVoiceNavigationKeys): NavBackStack<TxtVoiceNavigationKeys> {
    return rememberSerializable(serializer = serializer()) {
        NavBackStack(*elements)
    }
}

@Composable
fun TextVoiceAppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberMyAppNavBackStack(HomeTranscribeRoute)
    val currentDestination = backStack.lastOrNull() ?: HomeTranscribeRoute

    Scaffold(
        modifier = modifier.background(color = DarkBackground),
        topBar = {
            TxtVoiceHeader(
                currentDestination = currentDestination,
                canNavigateBack = backStack.size > 1,
                onBackClick = { backStack.removeLastOrNull() }
            )
        },
        bottomBar = { TxtVoiceBottomBar(backStack = backStack) }
    ) { innerPadding ->
        TxtVoiceNavDisplay(Modifier.padding(innerPadding), backStack)
    }
}

@Composable
fun TxtVoiceNavDisplay(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<TxtVoiceNavigationKeys>
) {
    NavDisplay(
        modifier = modifier.background(color = DarkBackground),
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                HomeTranscribeRoute -> NavEntry(key) {
                    HomeTranscribeScreen()
                }

//                SettingsRoute -> NavEntry(key) {
//                    SettingsScreen()
//                }

                SpeakRoute -> NavEntry(key) {
                    SpeakScreen()
                }

//                HistoryRoute -> NavEntry(key) {
//                    HistoryScreen()
//                }
            }
        }
    )
}

@Composable
fun TxtVoiceBottomBar(
    modifier: Modifier = Modifier,
    backStack: NavBackStack<TxtVoiceNavigationKeys>
) {
    val routes = listOf(
        HomeTranscribeRoute,
        SpeakRoute,
//        HistoryRoute,
//        SettingsRoute
    )

    NavigationBar(
        modifier = modifier,
        containerColor = DarkSurfaceVariant,//DarkBackground,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        routes.forEach { destination ->
            val isSelected = backStack.lastOrNull() == destination
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    // Prevent reloading if clicking the already selected tab
                    if (backStack.lastOrNull() != destination) {
                        // Clear stack back to the root tab to avoid infinite loops
                        backStack.clear()
                        backStack.add(destination)
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(destination.icon),
                        contentDescription = stringResource(id = destination.label)
                    )
                },
                label = { Text(text = stringResource(destination.label)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}

@Composable
fun TxtVoiceHeader(
    currentDestination: TxtVoiceNavigationKeys,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //  Wrap in a Surface or Row to design the persistent layout styling
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background // Inherits your locked dark theme
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 4.dp), // Fine-tuned alignment padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            //  Conditional Back Button
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else {
                // Provides uniform text indent alignment if there is no back arrow
                Spacer(modifier = Modifier.width(12.dp))
            }

            // Dynamic Screen Title extracted from string resources
            Text(
                text = stringResource(id = currentDestination.label),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            //  Placeholder slot for right-side visual accents (e.g. your signal indicator)
//             Icon(painter = painterResource(R.drawable.ic_signal), contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}