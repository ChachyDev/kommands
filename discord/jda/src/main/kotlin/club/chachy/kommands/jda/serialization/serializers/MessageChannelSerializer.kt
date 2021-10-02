package club.chachy.kommands.jda.serialization.serializers

import club.chachy.kommands.jda.context.JDAContext
import club.chachy.kommands.jda.serialization.JDASerializer
import net.dv8tion.jda.api.entities.MessageChannel

object MessageChannelSerializer : JDASerializer<MessageChannel> {
    private val mentionRegex = "<#(\\d{18,21})>".toRegex()

    override fun deserialize(data: String, context: JDAContext): MessageChannel? {
        data.toLongOrNull()?.let { return context.guild?.getTextChannelById(it) }

        return mentionRegex.find(data)?.groupValues?.let {
            context.guild?.getTextChannelById(
                it.getOrNull(1) ?: return@let null
            )
        }
    }
}