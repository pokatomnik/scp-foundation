package tk.pokatomnik.scpfoundation.pages

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tk.pokatomnik.scpfoundation.di.http.HttpClient
import javax.inject.Inject

@HiltViewModel
class PagesViewModel @Inject constructor(
    val httpClient: HttpClient
) : ViewModel()