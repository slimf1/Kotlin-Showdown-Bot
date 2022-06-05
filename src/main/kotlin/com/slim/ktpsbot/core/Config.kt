package com.slim.ktpsbot.core

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import java.io.FileReader

data class Config(
    val server: String,
    val port: Int,
    val username: String,
    val password: String,
    val rooms: List<String>,
    val defaultRoom: String,
    val roomBlacklist: List<String>,
    val whitelist: List<String>,
    val trigger: String
) {
    companion object {
        fun fromFile(path: String): Config {
            val config: Config?
            val fileReader = FileReader(path)
            fileReader.use {
                val jsonReader = JsonReader(fileReader)
                jsonReader.use {
                    config = Gson().fromJson<Config>(jsonReader, Config::class.java)
                }
            }
            return config!!
        }
    }
}
