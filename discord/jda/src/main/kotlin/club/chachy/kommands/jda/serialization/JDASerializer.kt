package club.chachy.kommands.jda.serialization

import club.chachy.kommands.argument.processor.serializer.Serializer
import club.chachy.kommands.jda.context.JDAContext

interface JDASerializer<T> : Serializer<T, JDAContext>