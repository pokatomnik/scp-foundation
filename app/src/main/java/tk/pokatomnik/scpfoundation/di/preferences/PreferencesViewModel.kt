package tk.pokatomnik.scpfoundation.di.preferences

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    val preferencesContainer: PreferencesContainer
) : ViewModel()