package tk.pokatomnik.scpfoundation.di.preferences

import android.content.Context

class PreferencesContainer(private val context: Context) {
    val pagesPreferences by lazy { PagesPreferences(context) }
}