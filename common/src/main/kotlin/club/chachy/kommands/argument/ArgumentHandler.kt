package club.chachy.kommands.argument

import club.chachy.kommands.command.Command

interface ArgumentHandler<C> {
    fun process(command: Command<*>, raw: List<String>, context: C)

    operator fun <T> get(name: String, clazz: Class<T>): T?
}
inline operator fun <reified T> ArgumentHandler<*>.get(name: String) = get(name, T::class.java)