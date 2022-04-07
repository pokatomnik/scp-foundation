package tk.pokatomnik.scpfoundation.di.db

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(
    val database: Database
) : ViewModel()