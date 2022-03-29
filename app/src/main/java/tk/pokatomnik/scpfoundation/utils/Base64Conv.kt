package tk.pokatomnik.scpfoundation.utils

import android.util.Base64

fun base64ToString(base64: String): String {
    return String(Base64.decode(base64, Base64.DEFAULT))
}

fun stringToBase64(input: String): String {
    return Base64.encodeToString(input.encodeToByteArray(), Base64.DEFAULT)
}