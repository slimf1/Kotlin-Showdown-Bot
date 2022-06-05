package com.slim.ktpsbot.client

import com.slim.ktpsbot.core.MessageHandler

interface Client {
    fun setMessageHandler(messageHandler: MessageHandler)
    fun connect(host: String, port: Int)
    fun send(message: String)
    fun login(username: String, password: String, challstr: String)
}
