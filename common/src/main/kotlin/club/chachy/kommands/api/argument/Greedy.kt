package club.chachy.kommands.api.argument

/**
 * Greedy takes all the remaining arguments for the argument/parameter given
 * if there are any parameters after this an exception WILL be thrown!
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Greedy
