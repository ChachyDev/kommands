package club.chachy.kommands.argument.processor

import club.chachy.kommands.argument.ArgumentHandler
import club.chachy.kommands.argument.processor.serializer.SerializationFactory
import club.chachy.kommands.argument.processor.serializer.Serializer
import club.chachy.kommands.argument.processor.serializer.default.DefaultSerializationFactory
import club.chachy.kommands.command.Command

class ArgumentProcessor<C>(
    private val serializationFactory: SerializationFactory = DefaultSerializationFactory()
) : ArgumentHandler<C> {
    data class Spec(val value: String?, val isRequired: Boolean)

    private val specs = HashMap<String, Spec>()

    private var ctx: C? = null

    override fun process(command: Command<*>, raw: List<String>, context: C) {
        ctx = context
        val commandSpec = command.spec.split(" ")

        commandSpec.forEachIndexed { index, s ->
            val clean = s.clean()

            val isVararg = clean.endsWith("...")

            if (index != (commandSpec.size - 1) && isVararg) {
                error("Varargs can only be used at the end of the spec!")
            }

            specs[clean.removeVararg()] = Spec(if (isVararg) raw.mapNotNull { if (raw.indexOf(it) >= index) it else null }.joinToString(" ") else raw.getOrNull(index), s.startsWith("<") && s.endsWith(">"))
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(name: String, clazz: Class<T>): T? {
        val value = specs[name] ?: return null
        if (value.isRequired && value.value == null) error("Could not find argument for $name")

        return (serializationFactory.serializers[clazz] as? Serializer<T, C>)?.serialize(value.value ?: return null, ctx!!)
    }
}

private fun String.clean(): String {
    return removePrefix("<")
        .removeSuffix(">")
        .removePrefix("[")
        .removeSuffix("]")
}

private fun String.removeVararg(): String {
    return removeSuffix("...")
}