package club.chachy.kommands.argument.processor.serializer

interface Serializer<T> {
    fun serialize(data: String): T?
}