package tk.pokatomnik.scpfoundation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FiberNew
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Tag
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import tk.pokatomnik.scpfoundation.domain.PageInfoImpl
import tk.pokatomnik.scpfoundation.features.favorites.FavoritesList
import tk.pokatomnik.scpfoundation.features.pagesproviders.LatestPagesProvider
import tk.pokatomnik.scpfoundation.features.page.Page
import tk.pokatomnik.scpfoundation.features.pagesproviders.MainPagesByRatingProvider
import tk.pokatomnik.scpfoundation.features.pageslist.PagesList
import tk.pokatomnik.scpfoundation.features.pagesproviders.MainPagesByTagsProvider
import tk.pokatomnik.scpfoundation.features.tags.Tags
import tk.pokatomnik.scpfoundation.ui.theme.SCPFoundationTheme
import tk.pokatomnik.scpfoundation.utils.deserializeFromURLFriendly
import tk.pokatomnik.scpfoundation.utils.serializeToURLFriendly

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
//                                        Disable "back to home" navigation
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.List,
                                        contentDescription = "Документы"
                                    )
                                }
                            )
                            BottomNavigationItem(
                                selected = currentDestination?.hierarchy?.any { it.route === "latestPages" } == true,
                                onClick = {
                                    navController.navigate("latestPages") {
//                                        Disable "back to home" navigation
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.FiberNew,
                                        contentDescription = "Новые документы"
                                    )
                                }
                            )
                            BottomNavigationItem(
                                selected = currentDestination?.hierarchy?.any { it.route === "favorites" } == true,
                                onClick = {
                                    navController.navigate("favorites") {
//                                        Disable "back to home" navigation
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Избранное"
                                    )
                                }
                            )
                            BottomNavigationItem(
                                selected = currentDestination?.hierarchy?.any { it.route === "tags" } == true,
                                onClick = {
                                    navController.navigate("tags") {
//                                        Disable "back to home" navigation
//                                        popUpTo(navController.graph.findStartDestination().id) {
//                                            saveState = true
//                                        }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(imageVector = Icons.Filled.Tag, contentDescription = "Теги")
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "pages",
                            Modifier.padding(innerPadding)
                        ) {
                            composable(route = "pages") {
                                MainPagesByRatingProvider {
                                    PagesList(
                                        title = "Список документов",
                                        emptyText = "Нет документов на этой странице",
                                        bottomText = { it.author ?: "(Автор неизвестен)" },
                                        onSelectPageInfo = {
                                            val route = "page/${serializeToURLFriendly(it.url)}/${serializeToURLFriendly(it.name)}"
                                            navController.navigate(route) {
//                                                Disable "back to home" navigation
//                                                popUpTo(navController.graph.findStartDestination().id) {
//                                                    saveState = true
//                                                }
                                                launchSingleTop = true
                                            }
                                        },
                                    )
                                }
                            }
                            composable(
                                route = "pagesByTags/{tags}",
                                arguments = listOf(navArgument("tags") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val tags = deserializeFromURLFriendly(
                                    backStackEntry.arguments?.getString("tags") ?: ""
                                ).split("|").toTypedArray()

                                MainPagesByTagsProvider(tags = tags) {
                                    PagesList(
                                        hideNavigation = true,
                                        title = "По тегам",
                                        emptyText = "Нет документов по выбранным тегам",
                                        bottomText = { null },
                                        onSelectPageInfo = {
                                            val route = "page/${serializeToURLFriendly(it.url)}/${serializeToURLFriendly(it.name)}"
                                            navController.navigate(route) {
//                                                    Disable "back to home" navigation
//                                                    popUpTo(navController.graph.findStartDestination().id) {
//                                                        saveState = true
//                                                    }
                                                launchSingleTop = true
                                            }
                                        }
                                    )
                                }
                            }
                            composable(route = "favorites") {
                                FavoritesList(onSelectPageInfo = {
                                    val route = "page/${serializeToURLFriendly(it.url)}/${serializeToURLFriendly(it.name)}"
                                    navController.navigate(route) {
//                                            Disable "back to home" navigation
//                                            popUpTo(navController.graph.findStartDestination().id) {
//                                                saveState = true
//                                            }
                                        launchSingleTop = true
                                    }
                                })
                            }
                            composable(route = "latestPages") {
                                LatestPagesProvider {
                                    PagesList(
                                        title = "Новые документы",
                                        emptyText = "Нет документов на этой странице",
                                        bottomText = { it.author ?: "(Автор неизвестен)" },
                                        onSelectPageInfo = {
                                            val route = "page/${serializeToURLFriendly(it.url)}/${serializeToURLFriendly(it.name)}"
                                            navController.navigate(route) {
//                                                Disable "back to home" navigation
//                                                popUpTo(navController.graph.findStartDestination().id) {
//                                                    saveState = true
//                                                }
                                                launchSingleTop = true
                                            }
                                        },
                                    )
                                }
                            }
                            composable(
                                route = "page/{urlURLFriendly}/{nameURLFriendly}",
                                arguments = listOf(
                                    navArgument("urlURLFriendly") {
                                        type = NavType.StringType
                                    },
                                    navArgument("nameURLFriendly") {
                                        type = NavType.StringType
                                    }
                                )
                            ) { backStackEntry ->
                                val arguments = backStackEntry.arguments
                                val url = arguments?.getString("urlURLFriendly")?.let {
                                    deserializeFromURLFriendly(it)
                                }
                                val name = arguments?.getString("nameURLFriendly")?.let {
                                    deserializeFromURLFriendly(it)
                                }
                                val pageInfo = if (url != null && name != null) {
                                    PageInfoImpl(name = name, url = url, date = null, rating = null, author = null)
                                } else null
                                Page(
                                    page = pageInfo,
                                    navigateBack = { navController.popBackStack() }
                                )
                            }
                            composable(
                                route = "tags"
                            ) {
                                Tags(onSelectTags = {
                                    val tagsSerialized = serializeToURLFriendly(it.joinToString("|"))
                                    navController
                                        .navigate("pagesByTags/${tagsSerialized}") {
//                                                Disable "back to home" navigation
//                                                popUpTo(navController.graph.findStartDestination().id) {
//                                                    saveState = true
//                                                }
                                            launchSingleTop = true
                                        }
                                }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
