package com.slim.ktpsbot

import com.slim.ktpsbot.client.ShowdownClient
import com.slim.ktpsbot.core.Bot
import com.slim.ktpsbot.core.Config

fun main() {
    val configResource = Config::class.java.classLoader.getResource("config.json")
        ?: return println("Could not find the config resource")
    val config = Config.fromFile(configResource.file)
    ShowdownClient().use {
        Bot(config, it).start()
    }
}
