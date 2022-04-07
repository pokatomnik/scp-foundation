package tk.pokatomnik.scpfoundation.pages

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tk.pokatomnik.scpfoundation.di.db.Database
import javax.inject.Inject

@HiltViewModel
class LazyPagesListViewModel @Inject constructor(
    val database: Database
) : ViewModel()