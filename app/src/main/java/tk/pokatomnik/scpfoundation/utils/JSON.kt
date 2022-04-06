package tk.pokatomnik.scpfoundation.utils

import com.google.gson.Gson

fun stringify(inputValue: Any) : String {
    val gson = Gson()
    return gson.toJson(inputValue)
}

inline fun <reified T> parse(jsonString: String): T {
    val gson = Gson()
    return gson.fromJson(jsonString, T::class.java)
}