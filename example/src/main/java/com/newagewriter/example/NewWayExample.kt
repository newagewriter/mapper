package com.newagewriter.example

import io.github.newagewriter.mapper.Mapper
import io.github.newagewriter.processor.mapper.AbstractMapper

@Mapper
data class NewWayModel(val id: Int, val name: String)

fun main(args: Array<String>) {
    AbstractMapper.of(NewWayModel::class.java)?.let { m ->
        val model = NewWayModel(111, "Test model")
        val map = m.toMap(model)
        println("map: $map")
        val json = m.toJson(model)
        println("json: $json")

        val model2 = NewWayModel(222, "Test model2")
        val map2 = m.toMap(model2)
        println("map: $map2")
        val json2 = m.toJson(model2)
        println("json: $json2")

        val jsonString = "{ \"id\" : 123, \"name\" : \"Test\" }"
        val modelFromJson = m.jsonToModel(json)

    }

    val jsonString = "{ \"id\" : 123, \"name\" : \"Test\" }"
    val modelFromJson = AbstractMapper.fromJsonToObject(NewWayModel::class.java, jsonString)
    println("model: $modelFromJson")
}
