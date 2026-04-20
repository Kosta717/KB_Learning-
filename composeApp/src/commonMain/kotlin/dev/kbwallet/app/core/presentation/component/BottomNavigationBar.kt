package dev.kbwallet.app.core.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.kbwallet.app.core.navigation.Dashboard
import dev.kbwallet.app.core.navigation.History
import dev.kbwallet.app.core.navigation.Portfolio
import dev.kbwallet.app.core.navigation.Profile

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val items = listOf(
            BottomNavItem("Dashboard", Dashboard, Icons.Default.Home),
            BottomNavItem("Portfolio", Portfolio, Icons.Default.ShowChart),
            BottomNavItem("History", History, Icons.Default.History),
            BottomNavItem("Profile", Profile, Icons.Default.Person)
        )

        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route?.contains(item.route::class.simpleName ?: "") == true } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Dashboard) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.name) },
                label = { Text(item.name) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

private data class BottomNavItem(
    val name: String,
    val route: Any,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
