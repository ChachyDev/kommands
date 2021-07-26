package club.chachy.kommands.jda

import club.chachy.kommands.argument.ArgumentHandler
import club.chachy.kommands.argument.processor.ArgumentProcessor
import club.chachy.kommands.handler.KommandHandler
import club.chachy.kommands.jda.context.JDAContext
import club.chachy.kommands.jda.serialization.JDASerializationFactory
import club.chachy.kommands.prefix.PrefixHandler
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommandListener(
    private val prefix: PrefixHandler<JDAContext>,
    val handler: KommandHandler<JDAContext> = KommandHandler(),
    private val delimiter: String = " ",
    private val argumentHandler: ArgumentHandler<JDAContext> = ArgumentProcessor(JDASerializationFactory()),
    private val exceptionHandler: Throwable.() -> Unit = { printStackTrace() }
) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        handler.handle(
            prefix,
            event.message.contentRaw,
            JDAContext(event.guild, event.message, event.channel, event.author),
            argumentHandler,
            exceptionHandler,
            delimiter
        )
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        handler.handle(
            prefix,
            event.message.contentRaw,
            JDAContext(event.guild, event.message, event.channel, event.author),
            argumentHandler,
            exceptionHandler,
            delimiter
        )
    }
}