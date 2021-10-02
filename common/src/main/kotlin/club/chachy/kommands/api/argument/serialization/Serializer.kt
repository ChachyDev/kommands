package club.chachy.kommands.api.argument.serialization

import club.chachy.kommands.api.context.CommandContext

interface Serializer<T, Context : CommandContext> {
    fun deserialize(data: String, context: Context): T?
}