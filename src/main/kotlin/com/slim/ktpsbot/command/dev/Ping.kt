package com.slim.ktpsbot.command.dev

import com.slim.ktpsbot.command.Command
import com.slim.ktpsbot.context.Context

class Ping : Command() {
    override val name: String = "ping"
    override val requiredRank: Char = '+'

    override fun run(context: Context) {
        context.reply("pong")
    }
}
