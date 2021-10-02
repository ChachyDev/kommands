package club.chachy.kommands.jda.permission

import club.chachy.kommands.api.permission.Permission
import club.chachy.kommands.jda.context.MessageContext
import net.dv8tion.jda.api.Permission as JDAPermissions

class JDAPermission(private vararg val permission: JDAPermissions) : Permission<MessageContext> {
    override fun accept(context: MessageContext): Boolean {
        return context.guild?.getMember(context.author)?.hasPermission(*permission) ?: false
    }
}