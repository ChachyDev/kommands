package club.chachy.kommands.argument.processor.serializer.default.serializers

import club.chachy.kommands.argument.processor.serializer.Serializer

class IntSerializer : Serializer<Int> {
    override fun serialize(data: String) = data.toIntOrNull()
}