package club.chachy.kommands.jda.serialization

import club.chachy.kommands.argument.processor.serializer.default.DefaultSerializationFactory
import club.chachy.kommands.jda.serialization.serializers.MemberSerializer
import club.chachy.kommands.jda.serialization.serializers.MessageChannelSerializer
import club.chachy.kommands.jda.serialization.serializers.UserSerializer

/**
 * The default serialization factory for JDA to add support for custom JDA types.
 */
class JDASerializationFactory : DefaultSerializationFactory() {
    init {
        registerSerializer(MessageChannelSerializer)
        registerSerializer(MemberSerializer)
        registerSerializer(UserSerializer)
    }
}