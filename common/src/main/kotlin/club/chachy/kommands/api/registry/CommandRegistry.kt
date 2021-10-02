package club.chachy.kommands.api.registry

import club.chachy.kommands.api.command.Command
import club.chachy.kommands.api.context.CommandContext

interface CommandRegistry {
    val commands: List<Command<*, *>>

    fun <T : Any?, C : CommandContext> register(command: Command<T, C>)
    fun lookup(name: String): Command<Any?, CommandContext>?

    operator fun get(name: String) = lookup(name)

    companion object Global : CommandRegistry {
        @Suppress("ObjectPropertyName")
        private val _commands = HashMap<String, Command<Any?, CommandContext>>()
        override val commands: List<Command<*, CommandContext>>
            get() = _commands.values.toList()

        @Suppress("UNCHECKED_CAST")
        override fun <T, C : CommandContext> register(command: Command<T, C>) {
            if (_commands.containsKey(command.name)) error("You cannot register a command twice!")
            _commands[command.name] = command as Command<Any?, CommandContext>
        }

        override fun lookup(name: String) = _commands[name]
    }
}