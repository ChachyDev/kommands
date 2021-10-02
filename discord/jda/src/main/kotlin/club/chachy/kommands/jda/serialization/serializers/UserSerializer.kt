package club.chachy.kommands.jda.serialization.serializers

import club.chachy.kommands.jda.context.JDAContext
import club.chachy.kommands.jda.serialization.JDASerializer
import net.dv8tion.jda.api.entities.User

object UserSerializer : JDASerializer<User> {
    val nameDiscrimRegex =
        "^(?!.*?```.*?#|.*? {2,}.*?#|everyone#|here#|discordtag#)([^@#:\\s][^@#:]*?[^@#:\\s])#(\\d{4})\$".toRegex()

    val mentionRegex = "<@!?(\\d{18,21})>".toRegex()

    override fun deserialize(data: String, context: JDAContext): User? {
        val jda = context.author.jda

        // Normal ID
        val id = data.toLongOrNull()
        if (id != null) {
            return context.guild?.getMemberById(id)?.user ?: jda.getUserById(id)
        }

        // Name#Disriminator
        val nameDiscrimGroup = nameDiscrimRegex.find(data)?.groupValues
        if (nameDiscrimGroup != null && nameDiscrimRegex.matches(data)) {
            val (name, discrim) = nameDiscrimGroup[1] to nameDiscrimGroup[2]
            return context.guild?.getMemberByTag(name, discrim)?.user ?: jda.getUserByTag(name, discrim)
        }

        // <@mention>
        val mentionGroup = mentionRegex.find(data)?.groupValues
        if (mentionGroup != null && mentionRegex.matches(data)) {
            val idFromMention = mentionGroup[1].toLongOrNull() ?: return null
            return context.guild?.getMemberById(idFromMention)?.user ?: jda.getUserById(idFromMention)
        }

        // Given up
        return null
    }
}