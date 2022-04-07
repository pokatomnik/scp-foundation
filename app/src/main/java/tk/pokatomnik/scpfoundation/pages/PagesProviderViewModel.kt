package tk.pokatomnik.scpfoundation.pages

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tk.pokatomnik.scpfoundation.di.db.Database
import tk.pokatomnik.scpfoundation.di.http.HttpClient
import tk.pokatomnik.scpfoundation.di.preferences.PreferencesContainer
import javax.inject.Inject

@HiltViewModel
class PagesProviderViewModel @Inject constructor(
    val httpClient: HttpClient,
    val preferencesContainer: PreferencesContainer,
) : ViewModel()