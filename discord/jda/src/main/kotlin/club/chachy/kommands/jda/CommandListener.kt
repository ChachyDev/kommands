package club.chachy.kommands.jda

import club.chachy.kommands.api.argument.serialization.SerializationFactory
import club.chachy.kommands.api.exception.IllegalCommandException
import club.chachy.kommands.api.executor.CommandExecutor
import club.chachy.kommands.api.prefix.PrefixHandler
import club.chachy.kommands.api.registry.CommandRegistry
import club.chachy.kommands.dsl.withExecutorContext
import club.chachy.kommands.jda.context.JDAContext
import club.chachy.kommands.jda.context.MessageContext
import club.chachy.kommands.jda.context.SlashCommandContext
import club.chachy.kommands.jda.serialization.JDASerializationFactory
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.utils.MarkdownUtil.codeblock
import java.awt.Color

class CommandListener(
    private val prefix: PrefixHandler,
    private val registry: CommandRegistry = CommandRegistry.Global,
    private val executor: CommandExecutor = CommandExecutor.Default,
    private val serializationFactory: SerializationFactory = JDASerializationFactory(),
    private val exceptionHandler: JDAContext.(Throwable) -> Unit = {
        channel.sendMessageEmbeds(
            EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Something went wrong :(")
                .setDescription("An error occured when trying to execute this command:\n${codeblock(it.message ?: it.stackTraceToString())}")
                .build()
        ).queue()

        if (it !is IllegalCommandException && it !is IllegalArgumentException) {
            it.printStackTrace()
        }
    }
) : ListenerAdapter() {
    override fun onMessageReceived(event: MessageReceivedEvent) {
        val context = MessageContext(event.guild, event.message, event.channel, event.author)
        withExecutorContext(executor, execptionHandler = { exceptionHandler(context, this) }) {
            execute(
                prefix,
                registry,
                event.message.contentRaw,
                context,
                serializationFactory
            )
        }
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        val context = MessageContext(event.guild, event.message, event.channel, event.author)
        withExecutorContext(executor, execptionHandler = { exceptionHandler(context, this) }) {
            execute(
                prefix,
                registry,
                event.message.contentRaw,
                context,
                serializationFactory
            )
        }
    }

    override fun onSlashCommand(event: SlashCommandEvent) {
        val context = SlashCommandContext(event.guild, event.channel, event.user, event)
        withExecutorContext(executor, execptionHandler = { exceptionHandler(context, this) }) {
            execute(
                registry,
                event.name,
                event.options.map { it.asString },
                context,
                serializationFactory
            )
        }
    }
}