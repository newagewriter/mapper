package com.newagewriter.example

import io.github.newagewriter.mapper.Mapper
import io.github.newagewriter.processor.mapper.AbstractMapper

@Mapper
data class ComplexData(val id: String, val name: String, val properties: Map<String, Any?>, val children: List<ComplexDataChildren>)

@Mapper
data class ComplexDataChildren(val id: Int, val name: String, val data: Any?)
object ComplexDataExample {
    @JvmStatic
    fun main(args: Array<String>) {
        println("Complex object example")
        AbstractMapper.of(ComplexData::class.java)?.let { m ->
            println("mapper: $m")
            ComplexDataExample::class.java.classLoader.getResourceAsStream("big_json_model.json")?.let { s ->
                val model = m.jsonToModel(s)
                println("model: $model")
            } ?: throw NullPointerException("Cannot load resources: big_json_model.json")
        } ?: throw NullPointerException("Cannot create mapper for: ${ComplexData::class.java.simpleName}")
    }
}
