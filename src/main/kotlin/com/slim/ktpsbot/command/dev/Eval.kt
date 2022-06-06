package com.slim.ktpsbot.command.dev

import com.slim.ktpsbot.command.Command
import com.slim.ktpsbot.context.Context
import javax.script.*


class Eval : Command() {
    override val name: String = "eval"
    override val wlOnly: Boolean = true
    override val aliases: Iterable<String> = listOf("exec")
    private val manager: ScriptEngineManager by lazy { ScriptEngineManager() }
    private val engine: ScriptEngine by lazy { manager.getEngineByName("js") }

    override fun run(context: Context) {
        try {
            val bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE)
            bindings.clear()
            bindings["context"] = context
            val result = engine.eval(context.target, bindings)
            context.reply("Result: $result")
        } catch (exception: ScriptException) {
            context.reply("An exception occurred: ${exception.message}")
        }
    }
}
