package com.slim.ktpsbot

import com.slim.ktpsbot.client.ShowdownClient
import com.slim.ktpsbot.core.Bot
import com.slim.ktpsbot.core.Config
import com.slim.ktpsbot.utils.Resource

fun main() {
    val configResource = Resource.loadResourceByName("config.json")
        ?: return println("Could not find the config resource. " +
                "Don't forget to rename config-example.json to config.json")
    val config = Config.fromFile(configResource.file)
    ShowdownClient().use {
        Bot(config, it).start()
    }
}
