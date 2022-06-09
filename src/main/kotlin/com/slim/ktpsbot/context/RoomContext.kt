package com.slim.ktpsbot.context

import com.slim.ktpsbot.core.Bot
import com.slim.ktpsbot.core.Room
import com.slim.ktpsbot.core.User

class RoomContext(
    bot: Bot,
    target: String,
    sender: User,
    command: String,
    private val room: Room,
    private val timestamp: Long
) : Context(bot, target, sender, command) {

    override val isPm: Boolean = false

    override val roomId: String = room.id

    override fun hasRank(requiredRank: Char): Boolean {
        if (sender.id in bot.config.whitelist) {
            return true
        }
        if (!RANKS.containsKey(requiredRank) || !RANKS.containsKey(sender.rank)) {
            return false;
        }
        return RANKS[sender.rank]!! >= RANKS[requiredRank]!!
    }

    override fun reply(message: String) {
        bot.say(roomId, message)
    }

    override fun sendHtml(html: String) {
        bot.say(roomId, "/addhtmlbox $html")
    }

    override fun sendUpdatableHtml(id: String, html: String, changes: Boolean) {
        val command = if (changes) "changeuhtml" else "adduhtml"
        bot.say(roomId, "/$command $id, $html")
    }
}
