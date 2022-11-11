package com.newagewriter.example

import com.newagewriter.example.model.User
import com.newagewriter.example.model.UserInfo
import com.newagewriter.example.model.mapper.UserMapper

object UserMapperExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val user = User(
            "user1",
            12345,
            UserInfo("http://avatar.url", 78f, System.currentTimeMillis()),
        )
        println("user: $user")

        val mapper = UserMapper(user)
        println("create mapper: $mapper")
        println("toMap: ${mapper.toMap()}")
        println("toJson: ${mapper.toJson()}")
    }
}