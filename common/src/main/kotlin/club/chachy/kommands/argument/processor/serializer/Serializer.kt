package club.chachy.kommands.argument.processor.serializer

interface Serializer<T, C> {
    fun serialize(data: String, context: C): T?
}