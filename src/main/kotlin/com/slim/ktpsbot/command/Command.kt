package com.slim.ktpsbot.command

import com.slim.ktpsbot.context.Context

abstract class Command {
    abstract val name: String
    open val aliases: Iterable<String> = emptyList()
    open val allowedInPm: Boolean = false
    open val wlOnly: Boolean = false
    open val pmOnly: Boolean = false
    open val requiredRank: Char = '&'
    open val helpMessage: String = ""

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
