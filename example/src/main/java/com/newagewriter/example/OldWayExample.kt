package com.newagewriter.example

import io.github.newagewriter.mapper.Mapper
import io.github.newagewriter.processor.mapper.AbstractMapper

@Mapper
data class OldWayModel(val id: Int, val name: String)

fun main(args: Array<String>) {
    val model = OldWayModel(111, "Test model")

    AbstractMapper.of(model)?.let { m ->
      val map = m.toMap()
      println("map: $map")
      val json = m.toJson()
      println("json: $json")
    }

    val model2 = OldWayModel(222, "Test model2")

    AbstractMapper.of(model2)?.let { m ->
        val map = m.toMap()
        println("map: $map")
        val json = m.toJson()
        println("json: $json")
    }

    val jsonString = "{ \"id\" : 123, \"name\" : \"Test\" }"
    val modelFromJson = AbstractMapper.fromJsonToObject(OldWayModel::class.java, jsonString)
    println("model: $modelFromJson")
}
