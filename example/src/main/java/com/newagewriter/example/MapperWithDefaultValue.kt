package com.newagewriter.example

import com.newagewriter.example.model.User

data class Test(
    val id: Int = -1,
    val name: String,
    val isValid: Boolean = true
)

object MapperWithDefaultValue {
    @JvmStatic
    fun main(args: Array<String>) {
        println("check mapper for class with defautl value")
        val clazz = Test::class
        clazz.constructors.forEach {
            println("check constructors: $it")
            it.parameters.forEach {
                println("param: $it, ${it.isOptional}")
            }
        }
    }
}