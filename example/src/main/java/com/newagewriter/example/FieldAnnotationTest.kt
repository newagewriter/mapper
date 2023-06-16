package com.newagewriter.example

import com.newagewriter.example.model.FieldTestModel
import io.github.newagewriter.processor.mapper.AbstractMapper

class FieldAnnotationTest {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val weirdMapFormat = mapOf<String, Any?>(
                "_id" to "1234567",
                "name" to "test1"
            )
            println("hello")
            AbstractMapper.toObject(FieldTestModel::class.java, weirdMapFormat)?.let { model ->
                println("id: ${model.id}, name: ${model.name}")

                val toMap = AbstractMapper.of(model)?.toMap()
                println("convert to map again: $toMap")
            }
        }
    }
}
