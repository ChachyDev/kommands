package club.chachy.kommands.argument.processor.serializer.default.serializers

import club.chachy.kommands.argument.processor.serializer.default.DefaultSerializer

object IntSerializer : DefaultSerializer<Int> {
    override fun serialize(data: String, context: Any?) = data.toIntOrNull()
}

object LongSerializer : DefaultSerializer<Long> {
    override fun serialize(data: String, context: Any?) = data.toLongOrNull()
}

object FloatSerializer : DefaultSerializer<Float> {
    override fun serialize(data: String, context: Any?) = data.toFloatOrNull()
}

object DoubleSerializer : DefaultSerializer<Double> {
    override fun serialize(data: String, context: Any?) = data.toDoubleOrNull()
}

object StringSerializer : DefaultSerializer<String> {
    override fun serialize(data: String, context: Any?) = data
}