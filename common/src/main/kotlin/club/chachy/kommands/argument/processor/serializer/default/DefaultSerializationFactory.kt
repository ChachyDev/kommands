package club.chachy.kommands.argument.processor.serializer.default

import club.chachy.kommands.argument.processor.serializer.SerializationFactory
import club.chachy.kommands.argument.processor.serializer.default.serializers.IntSerializer
import club.chachy.kommands.argument.processor.serializer.default.serializers.StringSerializer

class DefaultSerializationFactory : SerializationFactory() {
    init {
        registerSerializer(StringSerializer())
        registerSerializer(IntSerializer())
    }
}