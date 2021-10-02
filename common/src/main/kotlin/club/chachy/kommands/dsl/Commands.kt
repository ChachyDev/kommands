package club.chachy.kommands.dsl

import club.chachy.kommands.api.argument.NoArguments
import club.chachy.kommands.api.command.Command
import club.chachy.kommands.api.context.CommandContext
import club.chachy.kommands.api.exception.IllegalContextException
import club.chachy.kommands.api.exception.NoArgumentsAskedException
import club.chachy.kommands.api.executor.CommandExecutor
import club.chachy.kommands.api.permission.Permission
import club.chachy.kommands.api.registry.CommandRegistry
import kotlin.reflect.KFunction

data class CommandParams<Args, Context>(private val _args: Args?, val context: Context) {
    /**
     * To prevent (developers) having to mess with nullable types we
     * punish those who try to acesss unstated arguments by throwing
     * an exception.
     */
    val args: Args
        get() = _args ?: throw NoArgumentsAskedException("Attempted to access arguments when asked for none!")
}


inline fun <reified Context : CommandContext> CommandRegistry.command(
    name: String,
    description: String? = null,
    permissions: Collection<Permission<Context>> = setOf(),
    crossinline block: CommandParams<NoArguments, Context>.() -> Unit
) = command(name, description, null, permissions, block)


inline fun <reified Args, reified Context : CommandContext> CommandRegistry.command(
    name: String,
    description: String? = null,
    args: KFunction<Args>? = null,
    permissions: Collection<Permission<Context>> = setOf(),
    crossinline block: CommandParams<Args, Context>.() -> Unit
) {
    register(object : Command<Args, Context> {
        override val name = name
        override val description = description
        override val argumentsClass = args
        override val permissions: Collection<Permission<Context>> = permissions

        override fun onExecute(args: Args?, context: CommandContext) {
            if (context !is Context) {
                throw IllegalContextException("Failed to create context, two different context types were provided! (Expected: ${Context::class.java.name} but got ${context::class.java.name})")
            }

            block(CommandParams(args, context))
        }
    })
}

fun withRegistryContext(
    registry: CommandRegistry = CommandRegistry.Global,
    execptionHandler: Throwable.() -> Unit = { printStackTrace() },
    block: CommandRegistry.() -> Unit
) {
    try {
        block(registry)
    } catch (t: Throwable) {
        execptionHandler(t)
    }
}

fun withExecutorContext(
    executor: CommandExecutor = CommandExecutor.Default,
    execptionHandler: Throwable.() -> Unit = { printStackTrace() },
    block: CommandExecutor.() -> Unit
) {
    try {
        block(executor)
    } catch (t: Throwable) {
        execptionHandler(t)
    }
}