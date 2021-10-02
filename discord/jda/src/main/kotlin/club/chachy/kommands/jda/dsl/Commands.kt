package club.chachy.kommands.jda.dsl

import club.chachy.kommands.api.argument.NoArguments
import club.chachy.kommands.api.permission.Permission
import club.chachy.kommands.api.registry.CommandRegistry
import club.chachy.kommands.dsl.CommandParams
import club.chachy.kommands.dsl.command
import club.chachy.kommands.jda.context.MessageContext
import club.chachy.kommands.jda.context.SlashCommandContext
import club.chachy.kommands.jda.dsl.util.fillOptions
import club.chachy.kommands.jda.dsl.util.refillOptions
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.Command
import kotlin.reflect.KFunction

var slashCommands: MutableList<Command>? = null
var guildSlashCommands: MutableMap<Long, MutableList<Command>>? = null

inline fun <reified Args> CommandRegistry.textCommand(
    name: String,
    description: String? = null,
    args: KFunction<Args>,
    permissions: Collection<Permission<MessageContext>> = setOf(),
    parent: String? = null,
    crossinline block: CommandParams<Args, MessageContext>.() -> Unit
) = command(name, description, args, permissions, parent, block)

inline fun CommandRegistry.textCommand(
    name: String,
    description: String? = null,
    permissions: Collection<Permission<MessageContext>> = setOf(),
    parent: String? = null,
    crossinline block: CommandParams<NoArguments, MessageContext>.() -> Unit
) = command(name, description, permissions, parent, block)

inline fun <reified Args> CommandRegistry.slashCommand(
    name: String,
    description: String,
    jda: JDA,
    args: KFunction<Args>? = null,
    permissions: Collection<Permission<SlashCommandContext>> = setOf(),
    guild: Guild? = null,
    parent: String? = null,
    crossinline block: CommandParams<Args, SlashCommandContext>.() -> Unit
) {
    // TODO: Subcommand support as it's internally supported but not via slash commands
    // TODO: Rework messy system (current one)
    if (guild != null) {
        val guildCmds = commands(guild)
        val cmd = guildCmds[guild.idLong]?.find { it.name == name }
        if (cmd == null) {
            guild.upsertCommand(name, description).apply { args?.let { fillOptions(this, args) } }.queue()
        } else {
            if (args != null) {
                refillOptions(cmd, args)
            }
        }
    } else {
        val globalCmds = commands(jda)

        val cmd = globalCmds.find { it.name == name }
        if (cmd == null) {
            jda.upsertCommand(name, description).apply { args?.let { fillOptions(this, args) } }.queue()
        } else {
            if (args != null) {
                refillOptions(cmd, args)
            }
        }
    }

    if (args == null) {
        command(name, description, null, permissions, parent, block)
    } else {
        command(name, description, args, permissions, parent, block)
    }
}

inline fun CommandRegistry.slashCommand(
    name: String,
    description: String,
    jda: JDA,
    permissions: Collection<Permission<SlashCommandContext>> = setOf(),
    guild: Guild? = null,
    parent: String? = null,
    crossinline block: CommandParams<NoArguments, SlashCommandContext>.() -> Unit
) = slashCommand(name, description, jda, null, permissions, guild, parent, block)


fun commands(jda: JDA): MutableList<Command> {
    if (slashCommands == null) {
        slashCommands = jda.retrieveCommands().complete().toMutableList()
    }

    return slashCommands!!
}

fun commands(guild: Guild): Map<Long, MutableList<Command>> {
    if (guildSlashCommands == null) {
        guildSlashCommands =
            mutableMapOf(guild.idLong to guild.retrieveCommands().complete().toMutableList())
    }

    return guildSlashCommands!!
}
