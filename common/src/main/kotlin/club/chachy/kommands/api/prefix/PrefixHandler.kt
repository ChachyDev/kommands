package club.chachy.kommands.api.prefix

interface PrefixHandler {
    fun get(): List<String>

    fun startsWithAny(string: String) = get().any { string.startsWith(it) }
    fun removePrefix(string: String) =
        if (startsWithAny(string)) string.removePrefix(get().find { string.startsWith(it) }
            ?: error("This shouldn't happen... (Couldn't find any prefixes)")) else string
}