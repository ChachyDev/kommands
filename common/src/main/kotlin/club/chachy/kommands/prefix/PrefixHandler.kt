package club.chachy.kommands.prefix

interface PrefixHandler<T> {
    fun get(context: T): List<String>
}