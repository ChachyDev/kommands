package club.chachy.kommands.argument.processor.serializer.default.serializers

import club.chachy.kommands.argument.processor.serializer.Serializer

class StringSerializer : Serializer<String> {
    override fun serialize(data: String) = data
}