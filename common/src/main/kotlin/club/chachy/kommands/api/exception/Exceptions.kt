package club.chachy.kommands.api.exception

class IllegalCommandException(message: String) : Exception(message)
class IllegalParametersException(message: String) : Exception(message)
class IllegalSerializerStateException(message: String) : Exception(message)
class NoSerializersFoundException(message: String) : Exception(message)
class NoArgumentsAskedException(message: String) : Exception(message)
class IllegalContextException(message: String) : Exception(message)