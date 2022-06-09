package com.slim.ktpsbot.core

import com.slim.ktpsbot.client.Client
import com.slim.ktpsbot.command.Command
import com.slim.ktpsbot.context.RoomContext
import com.slim.ktpsbot.utils.Reflection
import com.slim.ktpsbot.utils.Text
import kotlin.system.exitProcess

class Bot(val config: Config, private val client: Client) {

    private var currentRoomId: String? = null
    private val rooms: MutableMap<String, Room> = HashMap()
    private val formats: MutableList<String> = ArrayList()
    private val commands: Map<String, Command> = Reflection
        .getCommandsFromPackage(Command::class.java.packageName)
        .fold(HashMap()) { map, type ->
            val command = type.getDeclaredConstructor().newInstance() // Should have default constructor
            map[command.name] = command
            command.aliases.forEach { map[it] = command }
            map
        }

    init {
        client.setMessageHandler(this::handleMessage)
    }

    private fun handleMessage(message: String) {
        val parts = message.split("\n")
        var room: String? = null
        if (parts.isNotEmpty() && parts[0][0] == '>') {
            room = parts[0].substring(1)
            currentRoomId = room
        }
        if (parts.size >= 2 && parts[1].startsWith("|init|chat")) {
            return loadRoom(room, message)
        }
        parts.forEach { handleReceivedLine(room, it) }
    }

    private fun loadRoom(roomId: String?, message: String) {
        println("Loading room : $roomId")
        val parts = message.split('\n')
        val roomTitle = parts[2].split('|')[2]
        val users = parts[3].split('|')[2].split(',') as MutableList<String>
        users.removeFirstOrNull()
        val room = Room(roomTitle, roomId)
        rooms[room.id] = room
        room.initializeUsers(users)
        println("Room $roomId : Done")
    }

    private fun handleReceivedLine(room: String?, line: String) {
        val roomId = room ?: currentRoomId
        val parts = line.split("|").toTypedArray()
        if (parts.size < 2) return

        // TODO : handle listeners

        when (parts[1]) {
            "nametaken" -> exitProcess(1)
            "updateuser" -> checkConnection(parts)
            "challstr" -> login(parts.copyOfRange(2, parts.size).joinToString("|"))
            "c:" -> handleChatMessage(parts[4], parts[3], roomId, parts[2].toLong())
            "J" -> rooms[room]?.joinUser(parts[2])
            "L" -> rooms[room]?.leaveUser(parts[2])
            "N" -> rooms[room]?.renameUser(parts[3], parts[2])
        }
    }

    private fun checkConnection(parts: Array<String>) {
        var name = parts[2].substring(1)
        if ('@' in name) name = name.dropLast(2)
        if (name != config.username) return
        println("Connection successful: Logged in as $name")
        for (room in config.rooms) {
            send("|/join $room")
            Thread.sleep(250)
        }
    }

    private fun handleChatMessage(message: String, sender: String, roomId: String?, timestamp: Long) {
        if (roomId == null || roomId in config.roomBlacklist || !rooms.containsKey(roomId)) return
        val room = rooms[roomId]!!
        val triggerLength = config.trigger.length
        if (message.substring(0, triggerLength) != config.trigger) return
        val text = message.substring(triggerLength)
        val spaceIndex = text.indexOf(' ')
        val command = if (spaceIndex > 0) text.subSequence(0, spaceIndex).toString() else text.trim().lowercase()
        if (!commands.containsKey(command)) return
        val target = if (spaceIndex > 0) text.substring(spaceIndex + 1) else ""
        val context = RoomContext(
            this,
            target,
            room.users[Text.toLowerAlphaNum(sender)]!!,
            command,
            room,
            timestamp
        )
        try {
            commands[command]!!.call(context)
        } catch (exception: Exception) {
            println("An exception occurred while executing a command")
            println(exception)
            println("Context: $context")
        }
    }

    private fun login(challstr: String) = client.login(config.username, config.password, challstr)

    fun start() = client.connect(config.server, config.port)

    fun send(message: String) = client.send(message)

    fun say(roomId: String, message: String) = send("$roomId|$message")
}
