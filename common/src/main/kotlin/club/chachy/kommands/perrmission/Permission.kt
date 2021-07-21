package club.chachy.kommands.perrmission

interface Permission<T> {
    fun isValid(context: T): Boolean
}

fun <T> Iterable<Permission<T>>.isValid(context: T): Boolean {
    for (permission in this) {
        if (!permission.isValid(context)) {
            return false
        }
    }

    return true
}