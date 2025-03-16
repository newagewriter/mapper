package com.newagewriter.example.errorhandling

import io.github.newagewriter.mapper.Mapper
import io.github.newagewriter.processor.mapper.AbstractMapper

object ErrorHandlingTest {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Test error handling")
        val jsonTest = """{ id : 9 }"""
        val model = AbstractMapper.fromJsonToObject(TestModel::class.java, jsonTest)
        model?.let {
            println("test model id: ${it.id}")
        } ?: println("cannot convert")

    }
}

@Mapper
data class TestModel(
    val id: String
)
