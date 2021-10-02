package club.chachy.kommands.api.executor

import club.chachy.kommands.api.argument.Greedy
import club.chachy.kommands.api.argument.serialization.SerializationFactory
import club.chachy.kommands.api.argument.serialization.Serializer
import club.chachy.kommands.api.argument.serialization.impl.ArraySerializer
import club.chachy.kommands.api.context.CommandContext
import club.chachy.kommands.api.exception.IllegalCommandException
import club.chachy.kommands.api.exception.IllegalParametersException
import club.chachy.kommands.api.exception.IllegalSerializerStateException
import club.chachy.kommands.api.exception.NoSerializersFoundException
import club.chachy.kommands.api.prefix.PrefixHandler
import club.chachy.kommands.api.registry.CommandRegistry
import kotlin.Array
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure
import kotlin.reflect.jvm.jvmName
import java.lang.reflect.Array as ReflectArray

interface CommandExecutor {
    fun execute(
        commandRegistry: CommandRegistry,
        name: String,
        args: List<String>,
        context: CommandContext,
        factory: SerializationFactory
    )

    fun execute(
        prefix: PrefixHandler,
        commandRegistry: CommandRegistry,
        text: String,
        context: CommandContext,
        factory: SerializationFactory
    ) {
        if (text.isNotEmpty() && prefix.startsWithAny(text)) {
            val name = prefix.removePrefix(text.split(" ")[0])
            val args = text.split(" ")
                .toMutableList()
                .apply { removeAt(0) }

            execute(commandRegistry, name, args, context, factory)
        }
    }

    companion object Default : CommandExecutor {
        override fun execute(
            commandRegistry: CommandRegistry,
            name: String,
            args: List<String>,
            context: CommandContext,
            factory: SerializationFactory
        ) {
            val cmd = commandRegistry.lookup(name) ?: throw IllegalCommandException("Unknown command ($name)")
            val possibleSubcommand = args[0]
            if (cmd.subcommands.containsKey(possibleSubcommand)) {
                execute(commandRegistry, possibleSubcommand, args.toMutableList().apply { removeAt(0) }, context, factory)
            } else {
                if (cmd.permissions.all { it.accept(context) }) {
                    val argsClass = cmd.argumentsClass?.let { createArgsClass(args, it, factory, context) }
                    cmd.onExecute(argsClass, context)
                }
            }
        }

        private fun createArgsClass(
            args: List<String>,
            kFunction: KFunction<Any?>,
            factory: SerializationFactory,
            context: CommandContext
        ): Any? {
            val deserializedArgs =
                deserialize0(
                    args,
                    kFunction.parameters.map { it.type.jvmErasure },
                    kFunction,
                    factory,
                    context,
                    varargCheck = { kFunction.parameters.getOrNull(it)?.isVararg ?: false },
                    greedyCheck = { kFunction.parameters.getOrNull(it)?.hasAnnotation<Greedy>() ?: false }
                ).toTypedArray()

            kFunction.isAccessible = true
            return kFunction.call(*deserializedArgs)
        }

        @Suppress("UNCHECKED_CAST")
        private fun deserialize0(
            args: List<String>,
            toTypes: List<KClass<*>>,
            func: KFunction<*>,
            factory: SerializationFactory,
            context: CommandContext,
            varargCheck: (Int) -> Boolean,
            greedyCheck: (Int) -> Boolean
        ): List<Any> {
            return toTypes.mapIndexed { index, type ->
                val isVararg = varargCheck(index)
                val isGreedy = greedyCheck(index)
                val rawArg = if (isVararg || isGreedy) {
                    if (isGreedy && toTypes.size - 1 > index) {
                        throw IllegalParametersException("Greedy CANNOT have any parameters! Either move them before or rethink your logic")
                    }

                    var filterIndex = 0
                    args.filter {
                        val accept = (index until args.size).contains(filterIndex)
                        filterIndex++
                        accept
                    }.joinToString(" ")
                } else {
                    args.getOrNull(index)
                        ?: throw IllegalArgumentException("Too little arguments specified, requires ${spec(func)}")
                }

                val serializer = (factory.serializers[type.java] ?: let {
                    if (type.java.isArray) {
                        createArraySerializer(type.java, factory)
                    } else {
                        throw NoSerializersFoundException("No serializer for requested type (${type.jvmName}) found")
                    }
                }) as Serializer<*, CommandContext>

                val serialized = serializer.deserialize(rawArg, context)
                    ?: throw IllegalSerializerStateException("Please make sure you have correclty entered the data")

                if (isVararg) {
                    val array = serialized as Array<*>
                    ReflectArray.newInstance(type.java.componentType, array.size).apply {
                        System.arraycopy(array, 0, this, 0, array.size)
                    }
                } else {
                    serialized
                }
            }
        }

        private fun spec(func: KFunction<*>): String {
            return func.parameters.joinToString(" ") {
                buildString {
                    append(it.takeIf { it.isOptional }?.let { "[" } ?: "<") // Opening

                    append(it.name)
                    append(it.takeIf { it.isVararg }?.let { "..." } ?: "")

                    append(it.takeIf { it.isOptional }?.let { "]" } ?: ">") // Closing
                }
            }
        }

        private fun createArraySerializer(
            clazz: Class<*>,
            factory: SerializationFactory
        ): Serializer<*, out CommandContext> {
            if (!factory.serializers.containsKey(clazz)) {
                val serializer = ArraySerializer(clazz.componentType.kotlin, factory)
                factory.serializers[clazz] = serializer
                return serializer
            }

            return factory.serializers[clazz]!!
        }
    }
}