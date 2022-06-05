package com.slim.ktpsbot.command

import com.slim.ktpsbot.context.Context

abstract class Command {
    abstract val name: String
    val aliases: Iterable<String> = emptyList()
    val allowedInPm: Boolean = false
    val wlOnly: Boolean = false
    val pmOnly: Boolean = false
    val requiredRank: Char = '&'
    val helpMessage: String = ""

    abstract fun run(context: Context)

    fun call(context: Context) {
        if ((!pmOnly || context.isPm)
            && (!context.isPm || allowedInPm || pmOnly)
            && (!wlOnly || context.bot.config.whitelist.contains(context.sender.id))
            && context.hasRank(requiredRank)
        ) {
            run(context)
        }
    }
}
