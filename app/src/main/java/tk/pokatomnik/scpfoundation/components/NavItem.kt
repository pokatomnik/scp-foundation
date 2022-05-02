package tk.pokatomnik.scpfoundation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun RowScope.SCPNavItem(
    navController: NavHostController,
    icon: ImageVector,
    contentDescription: String,
    staticRoute: String,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    BottomNavigationItem(
        selected = currentDestination?.hierarchy?.any { it.route === staticRoute } == true,
        onClick = {
            navController.navigate(staticRoute) {
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    )
}