package tk.pokatomnik.scpfoundation.di.http

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HttpClientViewModel @Inject constructor(
    val httpClient: HttpClient
) : ViewModel()