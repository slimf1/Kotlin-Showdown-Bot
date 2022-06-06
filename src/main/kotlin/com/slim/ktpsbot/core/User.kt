package com.slim.ktpsbot.core

import com.slim.ktpsbot.utils.Text

class User(name: String, val rank: Char) {

    val name: String
    val idle: Boolean
    val id = Text.toLowerAlphaNum(name)

    init {
        if (name.takeLast(2) == "@!") {
            this.name = name.dropLast(2)
            this.idle = true
        } else {
            this.name = name
            this.idle = false
        }
    }

    override fun toString(): String {
        return "User(rank=$rank, name='$name', idle=$idle, id='$id')"
    }
}
