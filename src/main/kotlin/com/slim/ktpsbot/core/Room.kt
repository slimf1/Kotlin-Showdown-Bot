package com.slim.ktpsbot.core

import com.slim.ktpsbot.utils.Text

class Room(val name: String, roomId: String? = null) {

    val id: String
    val users: MutableMap<String, User> = HashMap()

    init {
        id = roomId ?: Text.toLowerAlphaNum(name)
    }

    fun initializeUsers(users: Iterable<String>) = users.forEach(this::joinUser)

    fun joinUser(userName: String) {
        val user = User(userName.substring(1), userName[0])
        users[user.id] = user
    }

    fun leaveUser(userName: String) {
        val userId = Text.toLowerAlphaNum(userName)
        users.remove(userId)
    }

    fun renameUser(oldName: String, newName: String) {
        leaveUser(oldName)
        joinUser(newName)
    }
}
