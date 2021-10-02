package club.chachy.kommands

import club.chachy.kommands.api.argument.Greedy
import club.chachy.kommands.api.argument.serialization.SerializationFactory
import club.chachy.kommands.api.context.CommandContext
import club.chachy.kommands.api.prefix.DefaultPrefixHandler
import club.chachy.kommands.dsl.command
import club.chachy.kommands.dsl.withExecutorContext
import club.chachy.kommands.dsl.withRegistryContext

class TestArgument(val test: String, @Greedy val also: String)

fun main() {
    val prefixHandler = DefaultPrefixHandler("!")

    withRegistryContext {
        command("test", "A command description", ::TestArgument) {
            println("Test: ${args.test} Also: ${args.also}")
        }

        withExecutorContext {
            execute(
                prefixHandler,
                this@withRegistryContext,
                "!test test test test test uiuiui sexo",
                CommandContext.Default,
                SerializationFactory.Default
            )
        }
    }
}