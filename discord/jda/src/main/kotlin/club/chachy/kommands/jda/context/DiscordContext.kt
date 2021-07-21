package club.chachy.kommands.jda.context

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User

data class DiscordContext(val guild: Guild?, val message: Message, val channel: MessageChannel, val author: User)