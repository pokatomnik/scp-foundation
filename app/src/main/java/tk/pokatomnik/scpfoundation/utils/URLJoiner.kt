package tk.pokatomnik.scpfoundation.utils

fun joinURLParts(vararg urls: String): String {
    return urls.joinToString('/'.toString()) {
        it.trim('/')
    }
}