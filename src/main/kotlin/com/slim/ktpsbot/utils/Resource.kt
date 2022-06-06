package com.slim.ktpsbot.utils

import java.net.URL

object Resource {
    fun loadResourceByName(name: String): URL? {
        return Resource::class.java.classLoader.getResource(name)
    }
}
