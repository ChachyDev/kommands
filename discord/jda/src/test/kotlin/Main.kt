import club.chachy.kommands.argument.ArgumentHandler
import club.chachy.kommands.argument.get
import club.chachy.kommands.jda.CommandListener
import club.chachy.kommands.jda.command.DiscordCommand
import club.chachy.kommands.jda.context.DiscordContext
import club.chachy.kommands.jda.prefix.default.DefaultPrefixHandler
import net.dv8tion.jda.api.JDABuilder

fun main() {
    val listener = CommandListener(DefaultPrefixHandler(listOf(System.getProperty("kommands.jda.prefix", "k!"))))

    listener.handler.registerCommand(object : DiscordCommand {
        override val name = "test"

        override val spec = "<first> <second...>"

        override fun execute(args: ArgumentHandler, context: DiscordContext) {
            val first: String = args["first"]!!
            val second: String = args["second"]!!

            context.channel.sendMessage("First argument: `$first` Second argument: `$second`").queue()
        }
    })

    JDABuilder
        .createDefault(System.getProperty("kommands.jda.token"))
        .addEventListeners(listener)
        .build()
}