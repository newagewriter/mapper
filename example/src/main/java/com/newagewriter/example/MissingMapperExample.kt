package com.newagewriter.example

import com.newagewriter.example.model.broken.BrokenUser
import com.newagewriter.example.model.broken.BrokenUserInfo
import com.newagewriter.processor.mapper.AbstractMapper

object MissingMapperExample {

    @JvmStatic
    fun main(args: Array<String>) {
        println("Some of example that cannot be mapped")
        val wrongUser = BrokenUser("12345abc", BrokenUserInfo("dfkjdshfjds", "23a-adsa-sd-asds"))
        val wrongMapper = AbstractMapper.of(wrongUser)
        wrongMapper?.let {
            println("mapper to json: ${it.toJson()}")
            println("mapper to map: ${it.toMap()}")
        }
        
        val myMap = mapOf<String, Any?>(
            "id" to "1234-abcd",
            "info" to mapOf<String, Any?>(

            )
        )
    }
}