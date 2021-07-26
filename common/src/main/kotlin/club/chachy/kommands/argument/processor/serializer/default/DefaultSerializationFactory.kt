package club.chachy.kommands.argument.processor.serializer.default

import club.chachy.kommands.argument.processor.serializer.SerializationFactory
import club.chachy.kommands.argument.processor.serializer.default.serializers.*

open class DefaultSerializationFactory : SerializationFactory() {
    init {
        registerSerializer(StringSerializer)
        registerSerializer(DoubleSerializer)
        registerSerializer(FloatSerializer)
        registerSerializer(LongSerializer)
        registerSerializer(IntSerializer)
    }
}