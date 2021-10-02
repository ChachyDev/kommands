package club.chachy.kommands.jda.dsl.util

import club.chachy.kommands.api.argument.Greedy
import club.chachy.kommands.discord.common.argument.Description
import club.chachy.kommands.jda.utils.toOptionType
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction
import net.dv8tion.jda.api.requests.restaction.CommandEditAction
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

fun <Args> fillOptions(action: CommandCreateAction, args: KFunction<Args>) {
    args.parameters.forEach {
        if (it.hasAnnotation<Greedy>()) {
            throw IllegalArgumentException("You cannot use Greedy with slash commands")
        }

        val parameterDescription = it.findAnnotation<Description>()?.description
            ?: throw IllegalArgumentException("When using slash commands you MUST annotate your parameters with @Description")
        action.addOption(
            it.type.jvmErasure.toOptionType(),
            it.name?.lowercase() ?: error("Failed to get parameter name"),
            parameterDescription
        )
    }
}

fun <Args> fillOptions(action: CommandEditAction, args: KFunction<Args>) {
    args.parameters.forEach {
        if (it.hasAnnotation<Greedy>()) {
            throw IllegalArgumentException("You cannot use Greedy with slash commands")
        }

        val parameterDescription = it.findAnnotation<Description>()?.description
            ?: throw IllegalArgumentException("When using slash commands you MUST annotate your parameters with @Description")
        action.addOption(
            it.type.jvmErasure.toOptionType(),
            it.name?.lowercase() ?: error("Failed to get parameter name"),
            parameterDescription
        )
    }
}

fun <Args> refillOptions(command: Command, args: KFunction<Args>) {
    if (command.options != args.parameters.map { it.type.jvmErasure.toOptionType() }) {
        command.editCommand().clearOptions().apply { fillOptions(this, args) }.queue()
    }
}