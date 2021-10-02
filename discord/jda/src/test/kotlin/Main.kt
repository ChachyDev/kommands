import club.chachy.kommands.api.prefix.DefaultPrefixHandler
import club.chachy.kommands.discord.common.argument.Description
import club.chachy.kommands.dsl.withRegistryContext
import club.chachy.kommands.jda.CommandListener
import club.chachy.kommands.jda.dsl.slashCommand
import club.chachy.kommands.jda.dsl.textCommand
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.User

data class Args(@Description("Parameter tests") val parameterTest: User)

fun main() {
    val listener = CommandListener(DefaultPrefixHandler { listOf(System.getProperty("kommands.jda.prefix", "k!")) })
    val jda = JDABuilder
        .createDefault(System.getProperty("kommands.jda.token") ?: error("You must provide a token"))
        .addEventListeners(listener)
        .build()

    withRegistryContext {
        with(jda) {
            slashCommand(name = "test", description = "A test command", ::Args, jda = this) {
                context.reply("Author: ${context.author.asTag} Data: ${args.parameterTest.asTag}")
            }

            textCommand(name = "test1", "A test command", ::Args) {
                context.channel.sendMessage("Author: ${context.author.asTag} Data: ${args.parameterTest.asTag}").queue()
            }
        }
    }
}