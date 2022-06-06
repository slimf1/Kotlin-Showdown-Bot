package com.slim.ktpsbot.core.mock

import com.slim.ktpsbot.client.Client
import com.slim.ktpsbot.core.MessageHandler

class MockClient : Client {

    var connected = false
        private set
    var loggedIn = false
        private set

    val sentMessages: MutableList<String> = ArrayList()
    private var messageHandler: MessageHandler? = null

    override fun setMessageHandler(messageHandler: MessageHandler) {
        this.messageHandler = messageHandler
    }

    override fun connect(host: String, port: Int) {
        connected = true
    }

    override fun send(message: String) {
        sentMessages.add(message)
    }

    override fun login(username: String, password: String, challstr: String) {
        loggedIn = true
    }

    fun receivedMessage(message: String) {
        messageHandler?.handleMessage(message)
    }
}