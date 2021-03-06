package tk.pokatomnik.scpfoundation.di.preferences

internal interface SCPPreferences {
    operator fun set(key: String, value: String)

    operator fun get(key: String): String?

    fun has(key: String): Boolean
}