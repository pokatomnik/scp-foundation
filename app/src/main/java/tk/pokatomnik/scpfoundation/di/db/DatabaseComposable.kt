package tk.pokatomnik.scpfoundation.di.db

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun rememberDatabase(): Database {
    return hiltViewModel<DatabaseViewModel>().database
}