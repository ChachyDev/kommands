package club.chachy.kommands.dsl

import club.chachy.kommands.api.argument.NoArguments
import club.chachy.kommands.api.context.CommandContext
import club.chachy.kommands.api.permission.Permission
import club.chachy.kommands.api.registry.CommandRegistry
import kotlin.reflect.KFunction

inline fun <reified Args> CommandRegistry.command(
    name: String,
    description: String? = null,
    args: KFunction<Args>,
    permissions: Collection<Permission<CommandContext.Default>> = setOf(),
    parent: String? = null,
    crossinline block: CommandParams<Args, CommandContext.Default>.() -> Unit
) = command<Args, CommandContext.Default>(name, description, args, permissions, parent, block)

inline fun CommandRegistry.command(
    name: String,
    description: String? = null,
    permissions: Collection<Permission<CommandContext.Default>> = setOf(),
    parent: String? = null,
    crossinline block: CommandParams<NoArguments, CommandContext.Default>.() -> Unit
) = command<CommandContext.Default>(name, description, permissions, parent, block)