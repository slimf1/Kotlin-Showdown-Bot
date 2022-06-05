package com.slim.ktpsbot.context

import com.slim.ktpsbot.core.Bot
import com.slim.ktpsbot.core.User

abstract class Context(
    val bot: Bot,
    val target: String,
    val sender: User,
    val command: String
) {
    companion object {
        val RANKS = mapOf(
            ' ' to 0, '+' to 1,
            '%' to 2, '@' to 3,
            '*' to 4, '#' to 5,
            '&' to 6, '~' to 7
        )
    }

    abstract val isPm: Boolean
    abstract val roomId: String
    abstract fun hasRank(requiredRank: Char): Boolean
    abstract fun reply(message: String)
    abstract fun sendHtml(html: String)
    abstract fun sendUpdatableHtml(id: String, html: String, changes: Boolean)
}
