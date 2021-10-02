package club.chachy.kommands.api.command

import club.chachy.kommands.api.context.CommandContext
import club.chachy.kommands.api.permission.Permission
import kotlin.reflect.KFunction

interface Command<Arguments, Context : CommandContext> {
    val name: String
    val description: String?
    val argumentsClass: KFunction<Arguments>?
    val permissions: Collection<Permission<Context>>
        get() = setOf()
    val subcommands: MutableMap<String, Command<*, Context>>

    fun onExecute(args: Arguments?, context: CommandContext)
}