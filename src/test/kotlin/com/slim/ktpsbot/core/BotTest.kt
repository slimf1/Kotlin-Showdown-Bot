package com.slim.ktpsbot.core

import com.slim.ktpsbot.core.mock.MockClient
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BotTest {
    private var mockClient: MockClient? = null
    private var bot: Bot? = null

    @BeforeEach
    fun setUp() {
        mockClient = MockClient()
        val configResource = Config::class.java.classLoader.getResource("config-test.json")
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
            "|title|Test Room 2|\n" +
            "|users|4,*BotUser, Panur, RegularUser,#RoomOwner"
        )
    }

    @Test
    fun testPingCommand() {
        mockClient?.receivedMessage(">franais\n|c:|1| RegularUser|-ping")
        assertEquals(true, mockClient?.sentMessages?.isEmpty())
        mockClient?.receivedMessage(">franais\n|c:|1|+VoicedUser|-ping")
        assertEquals(1, mockClient?.sentMessages?.size)
    }
}
