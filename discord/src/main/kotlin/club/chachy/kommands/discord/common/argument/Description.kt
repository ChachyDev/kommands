package club.chachy.kommands.discord.common.argument

/**
 * Used to describe a parameter of a command via an annotation
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Description(val description: String)