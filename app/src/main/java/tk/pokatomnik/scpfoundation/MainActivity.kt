package tk.pokatomnik.scpfoundation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import tk.pokatomnik.scpfoundation.page.Page
import tk.pokatomnik.scpfoundation.pages.PagesList
import tk.pokatomnik.scpfoundation.pages.PagesProvider
import tk.pokatomnik.scpfoundation.ui.theme.SCPFoundationTheme
import tk.pokatomnik.scpfoundation.utils.base64ToString
import tk.pokatomnik.scpfoundation.utils.stringToBase64

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SCPFoundationTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            BottomNavigationItem(
                                selected = currentDestination?.hierarchy?.any { it.route === "pages" } == true,
                                onClick = {
                                    navController.navigate("pages") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(Icons.Filled.List, contentDescription = "Документы") },
                                label = { Text("Документы") },
                            )
                            BottomNavigationItem(
                                selected = currentDestination?.hierarchy?.any { it.route === "favorites" } == true,
                                onClick = {
                                    navController.navigate("favorites") {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = { Icon(Icons.Filled.Favorite, contentDescription = "Избранное") },
                                label = { Text("Избранное") },
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        PagesProvider {
                            NavHost(navController = navController, startDestination = "pages", Modifier.padding(innerPadding)) {
                                composable(route = "pages") {
                                    PagesList {
                                        navController.navigate("page/${stringToBase64(it)}") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                                composable(
                                    route = "page/{urlBase64}",
                                    arguments = listOf(navArgument("urlBase64") {
                                        type = NavType.StringType
                                    })
                                ) { backStackEntry ->
                                    val url = backStackEntry.arguments?.getString("urlBase64")?.let {
                                        base64ToString(it)
                                    }
                                    Page(url)
                                }
                                composable(route = "favorites") {
                                    Text("This is favorites")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
