package club.chachy.kommands.jda.context

import club.chachy.kommands.api.context.CommandContext
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

interface JDAContext : CommandContext {
    val guild: Guild?
    val channel: MessageChannel
    val author: User
}

data class MessageContext(
    override val guild: Guild?,
    val message: Message,
    override val channel: MessageChannel,
    override val author: User
) : JDAContext

data class SlashCommandContext(
    override val guild: Guild?,
    override val channel: MessageChannel,
    override val author: User,
    val _event: SlashCommandEvent
) : JDAContext {
    fun reply(text: String, ephemeral: Boolean = true) {
        _event.deferReply()
            .setEphemeral(ephemeral)
            .setContent(text)
            .queue()
    }
}