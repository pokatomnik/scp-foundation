package tk.pokatomnik.scpfoundation.di.http

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun rememberHttpClient(): HttpClient {
    return hiltViewModel<HttpClientViewModel>().httpClient
}