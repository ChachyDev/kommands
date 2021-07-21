package club.chachy.kommands.jda

import club.chachy.kommands.argument.ArgumentHandler
import club.chachy.kommands.argument.processor.ArgumentProcessor
import club.chachy.kommands.handler.KommandHandler
import club.chachy.kommands.jda.context.DiscordContext
import club.chachy.kommands.jda.prefix.PrefixHandler
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(
    private val prefix: PrefixHandler,
    val handler: KommandHandler<DiscordContext> = KommandHandler(),
    private val delimiter: String = " ",
    private val argumentHandler: ArgumentHandler = ArgumentProcessor(),
    private val exceptionHandler: Throwable.() -> Unit = { printStackTrace() }
) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val context = DiscordContext(event.guild, event.message, event.channel, event.author)
        handler.handle(
            prefix.get(context),
            event.message.contentRaw,
            context,
            argumentHandler,
            exceptionHandler,
            delimiter
        )
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        val context = DiscordContext(event.guild, event.message, event.channel, event.author)
        handler.handle(
            prefix.get(context),
            event.message.contentRaw,
            context,
            argumentHandler,
            exceptionHandler,
            delimiter
        )
    }
}