package com.slim.ktpsbot.command.dev

import com.slim.ktpsbot.command.Command
import com.slim.ktpsbot.context.Context
import javax.script.ScriptEngineManager

class Eval : Command() {
    override val name: String = "eval"
    override val wlOnly: Boolean = true
    override val aliases: Iterable<String> = listOf("exec")
    private val manager = ScriptEngineManager()
    private val engine = manager.getEngineByName("js")

    override fun run(context: Context) {
        try {
            val result = engine.eval(context.target)
            context.reply("Result: $result")
        } catch (exception: Exception) {
            context.reply("An exception occurred: ${exception.message}")
        }
    }
}