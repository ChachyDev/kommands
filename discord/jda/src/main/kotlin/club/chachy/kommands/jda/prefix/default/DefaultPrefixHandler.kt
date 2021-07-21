package club.chachy.kommands.jda.prefix.default

import club.chachy.kommands.jda.context.DiscordContext
import club.chachy.kommands.jda.prefix.PrefixHandler

class DefaultPrefixHandler(private val prefixes: List<String>) : PrefixHandler {
    override fun get(context: DiscordContext) = prefixes
}