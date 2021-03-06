package com.slim.ktpsbot.core

import com.slim.ktpsbot.core.mock.MockClient
import com.slim.ktpsbot.utils.Resource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BotTest {
    private var mockClient: MockClient? = null
    private var bot: Bot? = null

    @BeforeEach
    fun setUp() {
        mockClient = MockClient()
        val configResource = Resource.loadResourceByName("config-test.json")
        bot = Bot(Config.fromFile(configResource!!.file), mockClient!!)
        mockClient?.receivedMessage(
            ">franais\n" +
            "|init|chat\n" +
            "|title|Français|\n" +
            "|users|5,*BotUser,@Panur, RegularUser,#RoomOwner,+VoicedUser"
        )
        mockClient?.receivedMessage(
            ">botdevelopment\n" +
            "|init|chat\n" +
            "|title|Bot Development|\n" +
            "|users|4,*BotUser, Panur, RegularUser,#RoomOwner"
        )
    }

    @Test
    fun testPingCommand() {
        mockClient?.receivedMessage(">franais\n|c:|1| RegularUser|-ping")
        assertTrue(mockClient!!.sentMessages.isEmpty())
        mockClient?.receivedMessage(">franais\n|c:|1|+VoicedUser|-ping")
        assertEquals(1, mockClient?.sentMessages?.size)
        assertEquals("franais|pong", mockClient?.sentMessages?.get(0))
        mockClient?.receivedMessage(">botdevelopment\n|c:|1| Panur|-ping")
        assertEquals(2, mockClient?.sentMessages?.size)
        assertEquals("botdevelopment|pong", mockClient?.sentMessages?.get(1))
    }

    @Test
    fun testEvalCommand() {
        mockClient?.receivedMessage(">franais\n|c:|1|#RoomOwner|-eval 5*3")
        assertTrue(mockClient!!.sentMessages.isEmpty())
        mockClient?.receivedMessage(">botdevelopment\n|c:|1| Panur|-eval 4+2")
        assertEquals(1, mockClient?.sentMessages?.size)
        assertEquals("botdevelopment|Result: 6", mockClient?.sentMessages?.get(0))
        mockClient?.receivedMessage("botdevelopment\n|c:|1| Panur|-eval context.sender.id")
        assertEquals("botdevelopment|Result: panur", mockClient?.sentMessages?.get(1))
        mockClient?.receivedMessage("botdevelopment\n|c:|1| Panur|-eval context.reply('test')")
        assertEquals(4, mockClient?.sentMessages?.size)
        assertEquals("botdevelopment|test", mockClient?.sentMessages?.get(2))
        mockClient?.receivedMessage("botdevelopment\n|c:|1| Panur|-eval context.propertyThatDoesntExist.id")
        assertTrue(mockClient!!.sentMessages.last().startsWith("botdevelopment|An exception occurred"))
    }
}
