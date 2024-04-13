package com.newagewriter.example

import com.newagewriter.example.model.Plant
import com.newagewriter.example.model.PlantType
import io.github.newagewriter.processor.mapper.AbstractMapper
import java.awt.Color

object ModelToJsonExample {
    @JvmStatic
    fun main(args: Array<String>) {
        println("test model to json")
        val model = Plant(
            name = "Zamiokulkas",
            type = PlantType.SUCCULENT,
            maxHeight = 4f,
            Color.GREEN
        )

        val jsonString = AbstractMapper.of(Plant::class.java)?.toJson(model)
        println("json from model: $jsonString")
    }
}
