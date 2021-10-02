package club.chachy.kommands.jda.utils

import net.dv8tion.jda.api.entities.AbstractChannel
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.commands.OptionType
import kotlin.reflect.KClass

private val optionMappings = mapOf(
    Int::class.java to OptionType.INTEGER,
    String::class.java to OptionType.STRING,
    Boolean::class.java to OptionType.BOOLEAN,
    User::class.java to OptionType.USER,
    AbstractChannel::class.java to OptionType.CHANNEL,
    Role::class.java to OptionType.ROLE,
    IMentionable::class.java to OptionType.MENTIONABLE
)

fun KClass<*>.toOptionType() = optionMappings.entries.find { java.isAssignableFrom(it.key) }?.value ?: OptionType.UNKNOWN
