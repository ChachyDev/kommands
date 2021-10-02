package club.chachy.kommands.api.permission

interface Permission<T> {
    fun accept(context: T): Boolean
}