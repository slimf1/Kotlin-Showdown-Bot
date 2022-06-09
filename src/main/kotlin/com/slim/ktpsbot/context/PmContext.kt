package com.slim.ktpsbot.context

import com.slim.ktpsbot.core.Bot
import com.slim.ktpsbot.core.User

class PmContext(
    bot: Bot,
    target: String,
    sender: User,
    command: String
) : Context(bot, target, sender, command) {

    override val isPm: Boolean = true

    override val roomId: String = bot.config.defaultRoom

    override fun hasRank(requiredRank: Char): Boolean = true

    override fun reply(message: String) {
        bot.send("|/pm ${sender.id}, $message")
    }

    override fun sendHtml(html: String) {
        bot.say(bot.config.defaultRoom, "/pminfobox ${sender.id}, $html")
    }

    override fun sendUpdatableHtml(id: String, html: String, changes: Boolean) {
        val command = if (changes) "pmchangeuhtml" else "pmuhtml"
        bot.say(bot.config.defaultRoom, "/$command ${sender.id}, $id, $html")
    }
}
