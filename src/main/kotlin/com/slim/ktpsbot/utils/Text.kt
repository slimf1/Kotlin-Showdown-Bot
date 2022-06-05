package com.slim.ktpsbot.utils

object Text {
    fun toLowerAlphaNum(text: String): String {
        return text.lowercase().replace("[^\\w]".toRegex(), "")
    }
}
