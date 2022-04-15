package tk.pokatomnik.scpfoundation.features.tags

val CYRILLIC = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя".uppercase().split("").toSet()

fun groupTags(list: Collection<String>): Map<String, List<String>> {
    return list
        .fold(listOf(mutableListOf<String>(), mutableListOf<String>())) { acc, current ->
            acc.apply {
                if (current.isEmpty()) {
                    acc.apply {
                        get(1).add(current)
                    }
                } else {
                    acc.apply {
                        val firstLetter = current[0].toString().uppercase()
                        val indexInList = if (CYRILLIC.contains(firstLetter)) 0 else 1
                        get(indexInList).add(current)
                    }
                }
            }
        }.let { (a, b) ->
            mutableListOf<String>()
                .apply {
                    addAll(a)
                    addAll(b)
                }
        }
        .fold(mutableMapOf<String, MutableList<String>>()) { map, current ->
            if (current.isBlank()) map else {
                map.apply {
                    val firstCharKey = current[0].uppercase()
                    map[firstCharKey] = (map[firstCharKey] ?: mutableListOf()).apply { add(current) }
                }
            }
        }
}