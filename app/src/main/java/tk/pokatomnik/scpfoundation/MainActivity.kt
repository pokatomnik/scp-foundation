package tk.pokatomnik.scpfoundation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Text
import androidx.compose.ui.text.style.TextAlign
import dagger.hilt.android.AndroidEntryPoint
import tk.pokatomnik.scpfoundation.features.configuration.ConfigurationProvider
import tk.pokatomnik.scpfoundation.features.splash.SplashScreen
import tk.pokatomnik.scpfoundation.ui.theme.SCPFoundationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private fun showErrorToast() {
        Toast.makeText(this, "Проблемы с загрузкой настроек", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SCPFoundationTheme {
                val isDarkTheme = isSystemInDarkTheme()
                ConfigurationProvider(
                    errorSideEffect = { showErrorToast() },
                    renderOnConfigurationExists = { Root() },
                    renderOnLoading = {
                        SplashScreen(
                            imageResourceId = if (isDarkTheme) R.drawable.scp_logo_white else R.drawable.scp_logo_black,
                            contentDescription = "Логотип SCP",
                            title = {
                                Text(
                                    text = "Получение доступа к секретной базе...",
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    },
                    renderOnError = {
                        SplashScreen(
                            imageResourceId = if (isDarkTheme) R.drawable.error_splash_white else R.drawable.error_splash_black,
                            contentDescription = "Ошибка загрузки приложения",
                            title = {
                                Text(
                                    text = "Ошибка загрузки приложения",
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Попробуйте открыть приложение позднее",
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}
