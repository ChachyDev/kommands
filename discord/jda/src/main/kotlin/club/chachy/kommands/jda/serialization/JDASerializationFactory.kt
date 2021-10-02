package club.chachy.kommands.jda.serialization

import club.chachy.kommands.api.argument.serialization.SerializationFactory
import club.chachy.kommands.jda.serialization.serializers.MemberSerializer
import club.chachy.kommands.jda.serialization.serializers.MessageChannelSerializer
import club.chachy.kommands.jda.serialization.serializers.UserSerializer

/**
 * The default serialization factory for JDA to add support for custom JDA types.
 */
class JDASerializationFactory : SerializationFactory() {
    init {
        registerAll(Default)
        register(MessageChannelSerializer)
        register(MessageChannelSerializer)
        register(MemberSerializer)
        register(UserSerializer)
    }
}