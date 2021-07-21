package club.chachy.kommands.jda.prefix

import club.chachy.kommands.jda.context.DiscordContext

interface PrefixHandler {
    fun get(context: DiscordContext): List<String>
}