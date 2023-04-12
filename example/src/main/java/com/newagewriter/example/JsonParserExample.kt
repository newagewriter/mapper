package com.newagewriter.example

import com.newagewriter.example.model.User
import io.github.newagewriter.processor.mapper.AbstractMapper

object JsonParserExample {
    @JvmStatic
    fun main(args: Array<String>) {
        println("check json to object parser")
        JsonParserExample::class.java.classLoader.getResourceAsStream("single_user.json")?.let {s ->
            val user = AbstractMapper.fromJsonToObject(User::class.java, s, Charsets.UTF_8)
            println("user: $user")
        }

        println("check parse array from json to list of object")
        JsonParserExample::class.java.classLoader.getResourceAsStream("users.json")?.let {s ->
            val users = AbstractMapper.fromJsonToArray(User::class.java, s, Charsets.UTF_8)
            users.forEach {
                println("user: $it")
            }
        }
    }
}
