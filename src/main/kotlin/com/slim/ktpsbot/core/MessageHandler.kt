package com.slim.ktpsbot.core

fun interface MessageHandler {
    fun handleMessage(message: String)
}
