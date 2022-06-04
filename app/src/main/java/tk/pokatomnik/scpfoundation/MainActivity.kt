package tk.pokatomnik.scpfoundation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import tk.pokatomnik.scpfoundation.features.configuration.ConfigurationProvider
import tk.pokatomnik.scpfoundation.ui.theme.SCPFoundationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SCPFoundationTheme {
                ConfigurationProvider(
                    renderOnConfigurationExists = {
                        Root()
                    }
                )
            }
        }
    }
}
