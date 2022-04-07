package tk.pokatomnik.scpfoundation.di.preferences

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun rememberPreferences(): PreferencesContainer {
    return hiltViewModel<PreferencesViewModel>().preferencesContainer
}