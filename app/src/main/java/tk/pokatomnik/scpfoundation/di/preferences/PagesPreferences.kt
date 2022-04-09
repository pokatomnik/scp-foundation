package tk.pokatomnik.scpfoundation.di.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlin.math.max

private class PagesScope(context: Context) : SCPPreferences {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("preferences.pages", MODE_PRIVATE)

    override operator fun set(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun get(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun has(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
}

class PagesPreferences(context: Context) {
    private val pagesScope = PagesScope(context)

    fun getSavedPage(): Int {
        val savedPage = pagesScope[KEY_PAGE]
        return  savedPage?.let {
            savedPage.toInt()
        } ?: 1
    }

    fun savePage(page: Int) {
        val pageToSave = max(0, page)
        pagesScope[KEY_PAGE] = pageToSave.toString()
    }

    private companion object {
        private const val KEY_PAGE = "savedPage"
    }
}