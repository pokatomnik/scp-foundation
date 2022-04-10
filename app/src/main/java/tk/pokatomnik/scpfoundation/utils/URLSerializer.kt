package tk.pokatomnik.scpfoundation.utils

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun deserializeFromURLFriendly(urlFriendly: String): String {
    return URLDecoder.decode(urlFriendly, StandardCharsets.UTF_8.toString())
}

fun serializeToURLFriendly(urlFriendly: String): String {
    return URLEncoder.encode(urlFriendly, StandardCharsets.UTF_8.toString())
}