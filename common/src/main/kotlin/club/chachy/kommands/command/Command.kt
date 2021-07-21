package club.chachy.kommands.command

import club.chachy.kommands.argument.ArgumentHandler
import club.chachy.kommands.perrmission.Permission

interface Command<T> {
    val name: String

    val spec: String

    val permissions: List<Permission<T>>
        get() = listOf()

    fun execute(args: ArgumentHandler, context: T)
}