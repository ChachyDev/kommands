package club.chachy.kommands.exception

class UnknownCommandException(name: String) : Exception("kommands: $name: command not found")