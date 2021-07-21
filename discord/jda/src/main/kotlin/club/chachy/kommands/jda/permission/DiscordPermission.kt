package club.chachy.kommands.jda.permission

import club.chachy.kommands.jda.context.DiscordContext
import club.chachy.kommands.perrmission.Permission
import net.dv8tion.jda.api.Permission as JDAPermission

class DiscordPermission(private vararg val permission: JDAPermission) : Permission<DiscordContext> {
    override fun isValid(context: DiscordContext): Boolean {
        return context.guild?.getMember(context.author)?.hasPermission(*permission) ?: false
    }
}