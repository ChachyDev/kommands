package club.chachy.kommands.api.argument.serialization.impl

import club.chachy.kommands.api.argument.serialization.SerializationFactory
import club.chachy.kommands.api.argument.serialization.Serializer
import club.chachy.kommands.api.context.CommandContext
import club.chachy.kommands.api.exception.NoSerializersFoundException
import kotlin.reflect.KClass

interface CommonSerializer<T> : Serializer<T, CommandContext>

class StringSerializer : CommonSerializer<String> {
    override fun deserialize(data: String, context: CommandContext) = data
}

class IntSerializer : CommonSerializer<Int> {
    override fun deserialize(data: String, context: CommandContext) = data.toIntOrNull()
}

class ArraySerializer<T : Any>(private val type: KClass<T>, private val factory: SerializationFactory) :
    CommonSerializer<Array<T>> {
    @Suppress("UNCHECKED_CAST")
    override fun deserialize(data: String, context: CommandContext): Array<T> {
        return data.split(" ")
            .map { (factory.serializers[type.java] as? Serializer<Any?, CommandContext>)?.deserialize(it, context) ?: throw NoSerializersFoundException("Unknown serializer") }
            .toTypedArray() as Array<T>
    }
}
