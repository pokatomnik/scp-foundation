package tk.pokatomnik.scpfoundation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import tk.pokatomnik.scpfoundation.page.Page
import tk.pokatomnik.scpfoundation.pages.PagesList
import tk.pokatomnik.scpfoundation.ui.theme.SCPFoundationTheme
import tk.pokatomnik.scpfoundation.utils.base64ToString
import tk.pokatomnik.scpfoundation.utils.stringToBase64

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SCPFoundationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "pages") {
                        composable("pages") {
                            PagesList { navController.navigate("page/${stringToBase64(it)}") }
                        }
                        composable(
                            "page/{urlBase64}",
                            arguments = listOf(navArgument("urlBase64") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val url = backStackEntry.arguments?.getString("urlBase64")?.let {
                                base64ToString(it)
                            }
                            Page(url)
                        }
                    }
                }
            }
        }
    }
}
