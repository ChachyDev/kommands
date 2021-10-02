package club.chachy.kommands.api.argument

/**
 * [NoArguments] is a placeholder class to specify that you want to no arguments. You do not specifically have to use
 * [NoArguments] however it's very helpful. There is no special logic behind it. If you TRY to call it when using a state
 * where arguments aren't specified you will FALL into an error at runtime
 */
class NoArguments private constructor()