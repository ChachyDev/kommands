package club.chachy.kommands.jda.serialization.serializers

import club.chachy.kommands.jda.context.JDAContext
import club.chachy.kommands.jda.serialization.JDASerializer
import club.chachy.kommands.jda.serialization.serializers.UserSerializer.mentionRegex
import club.chachy.kommands.jda.serialization.serializers.UserSerializer.nameDiscrimRegex
import net.dv8tion.jda.api.entities.Member

object MemberSerializer : JDASerializer<Member> {
    override fun serialize(data: String, context: JDAContext): Member? {
        if (context.guild == null) return null // Shouldn't be fetching a Member without a guild...

        // Normal ID
        val id = data.toLongOrNull()
        if (id != null) {
            return context.guild.getMemberById(id)
        }

        // Name#Disriminator
        val nameDiscrimGroup = nameDiscrimRegex.find(data)?.groupValues
        if (nameDiscrimGroup != null && nameDiscrimRegex.matches(data)) {
            val (name, discrim) = nameDiscrimGroup[1] to nameDiscrimGroup[2]
            return context.guild.getMemberByTag(name, discrim)
        }

        // <@mention>
        val mentionGroup = mentionRegex.find(data)?.groupValues
        if (mentionGroup != null && mentionRegex.matches(data)) {
            val idFromMention = mentionGroup[1].toLongOrNull() ?: return null
            return context.guild.getMemberById(idFromMention)
        }

        // Given up
        return null
    }
}