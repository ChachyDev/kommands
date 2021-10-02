package club.chachy.kommands.jda.serialization

import club.chachy.kommands.api.argument.serialization.Serializer
import club.chachy.kommands.jda.context.JDAContext

interface JDASerializer<T> : Serializer<T, JDAContext>