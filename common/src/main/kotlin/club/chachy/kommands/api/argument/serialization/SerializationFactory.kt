package club.chachy.kommands.api.argument.serialization

import club.chachy.kommands.api.argument.serialization.impl.IntSerializer
import club.chachy.kommands.api.argument.serialization.impl.StringSerializer
import club.chachy.kommands.api.context.CommandContext

abstract class SerializationFactory {
    val serializers = HashMap<Class<*>, Serializer<*, out CommandContext>>()

    inline fun <reified T, C : CommandContext> register(serializer: Serializer<T, C>) {
        serializers[T::class.java] = serializer
    }

    fun registerAll(factory: SerializationFactory) {
        factory.serializers.forEach {
            serializers[it.key] = it.value
        }
    }

    companion object Default : SerializationFactory() {
        init {
            register(StringSerializer())
            register(IntSerializer())
        }
    }
}