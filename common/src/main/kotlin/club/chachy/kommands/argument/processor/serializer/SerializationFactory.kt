package club.chachy.kommands.argument.processor.serializer

abstract class SerializationFactory {
    val serializers: MutableMap<Class<*>, Serializer<*>> = HashMap()

    inline fun <reified T> registerSerializer(serializer: Serializer<T>) {
        serializers[T::class.java] = serializer
    }
}