package com.slim.ktpsbot.client

import com.google.gson.JsonParser
import com.slim.ktpsbot.core.MessageHandler
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking

class ShowdownClient : Client, AutoCloseable {

    private var messageHandler: MessageHandler? = null
    private var wsSession: DefaultClientWebSocketSession? = null
    private val client = HttpClient {
        install(WebSockets)
        install(HttpTimeout) {
            requestTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
            connectTimeoutMillis = HttpTimeout.INFINITE_TIMEOUT_MS
        }
    }

    override fun setMessageHandler(messageHandler: MessageHandler) {
        this.messageHandler = messageHandler
    }

    override fun connect(host: String, port: Int) {
        runBlocking {
            client.webSocket(host = host, port = port, path = "/showdown/websocket") {
                wsSession = this
                while (true) {
                    val receivedFrame = incoming.receive() as? Frame.Text ?: continue
                    val message = receivedFrame.readText()
                    println("[RECV] $message")
                    messageHandler?.handleMessage(message)
                }
            }
        }
    }

    override fun send(message: String) {
        runBlocking {
            println("[SEND] $message")
            wsSession?.send(message)
        }
    }

    override fun login(username: String, password: String, challstr: String) {
        runBlocking {
            val response: HttpResponse = client.submitForm(
                url = "https://play.pokemonshowdown.com/~~showdown/action.php",
                formParameters = Parameters.build {
                    append("act", "login")
                    append("name", username)
                    append("pass", password)
                    append("challstr", challstr)
                },
            )
            val nonce = JsonParser
                .parseString(response.bodyAsText().substring(1))
                .asJsonObject
                .get("assertion")
                .asString
            send("|/trn ${username},0,${nonce}")
        }
    }

    override fun close() {
        runBlocking {
            wsSession?.close()
        }
        client.close()
    }
}
