package club.chachy.kommands.prefix.default

import club.chachy.kommands.prefix.PrefixHandler

class DefaultPrefixHandler<T>(private val prefixes: () -> List<String>) : PrefixHandler<T> {
    override fun get(context: T) = prefixes()
}