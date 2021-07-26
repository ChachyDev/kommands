package club.chachy.kommands.handler

import club.chachy.kommands.argument.ArgumentHandler
import club.chachy.kommands.command.Command
import club.chachy.kommands.exception.UnknownCommandException
import club.chachy.kommands.perrmission.isValid
import club.chachy.kommands.prefix.PrefixHandler

open class KommandHandler<T> {
    private val commands: MutableMap<String, Command<T>> = HashMap()

    fun handle(
        prefix: PrefixHandler<T>,
        message: String,
        context: T,
        handler: ArgumentHandler<T>,
        exceptionHandler: Throwable.() -> Unit,
        delimiter: String = " "
    ) {
        val prefixes = prefix.get(context)

        try {
            if (message.startsWith(prefixes)) {
                val (name, args) = message
                    .removePrefix(prefixes)
                    .takeIf { it.isNotEmpty() }
                    ?.split(delimiter)
                    ?.toMutableList()
                    ?.let { it.removeAt(0) to it }
                    ?: return

                val command = commands[name] ?: throw UnknownCommandException(name)

                if (command.permissions.isValid(context)) {
                    handler.process(command, args, context)
                    command.execute(handler, context)
                }
            }
        } catch (t: Throwable) {
            exceptionHandler(t)
        }
    }

    fun registerCommand(command: Command<T>) {
        commands[command.name] = command
    }

    private fun String.startsWith(prefixes: List<String>): Boolean {
        for (prefix in prefixes) {
            if (!startsWith(prefix)) {
                return false
            }
        }

        return true
    }

    private fun String.removePrefix(prefixes: List<String>): String {
        var newText = this

        for (prefix in prefixes) {
            newText = newText.removePrefix(prefix)
        }

        return newText
    }
}