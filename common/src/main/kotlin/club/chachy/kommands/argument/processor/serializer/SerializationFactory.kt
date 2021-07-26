package club.chachy.kommands.argument.processor.serializer

abstract class SerializationFactory {
    val serializers: MutableMap<Class<*>, Serializer<*, *>> = HashMap()

    inline fun <reified T, C> registerSerializer(serializer: Serializer<T, C>) {
        serializers[T::class.java] = serializer
    }
}