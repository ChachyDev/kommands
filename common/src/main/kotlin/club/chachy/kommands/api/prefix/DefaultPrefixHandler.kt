package club.chachy.kommands.api.prefix

class DefaultPrefixHandler(provider: () -> List<String>) : PrefixHandler {
    constructor(prefix: String) : this({ listOf(prefix) })

    private var cachedProvider = provider()
    override fun get() = cachedProvider
}