package club.chachy.kommands.jda.permission

import club.chachy.kommands.jda.context.JDAContext
import club.chachy.kommands.perrmission.Permission
import net.dv8tion.jda.api.Permission as JDAPermissions

class JDAPermission(private vararg val permission: JDAPermissions) : Permission<JDAContext> {
    override fun isValid(context: JDAContext): Boolean {
        return context.guild?.getMember(context.author)?.hasPermission(*permission) ?: false
    }
}