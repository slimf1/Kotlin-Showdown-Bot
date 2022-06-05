package com.slim.ktpsbot.core.mock

import com.slim.ktpsbot.client.Client
import com.slim.ktpsbot.core.MessageHandler

class MockClient : Client {

    val sentMessages: MutableList<String> = ArrayList()
    private var messageHandler: MessageHandler? = null

    override fun setMessageHandler(messageHandler: MessageHandler) {
        this.messageHandler = messageHandler
    }

    override fun connect(host: String, port: Int) {
    }

    override fun send(message: String) {
        sentMessages.add(message)
    }

    override fun login(username: String, password: String, challstr: String) {
    }

    fun receivedMessage(message: String) {
        messageHandler?.handleMessage(message)
    }
}