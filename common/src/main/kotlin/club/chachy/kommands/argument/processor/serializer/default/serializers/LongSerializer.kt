package club.chachy.kommands.argument.processor.serializer.default.serializers

import club.chachy.kommands.argument.processor.serializer.Serializer

class LongSerializer : Serializer<Long> {
    override fun serialize(data: String) = data.toLongOrNull()
}