package com.newagewriter.example

import com.newagewriter.example.model.*
import com.newagewriter.example.model.mapper.CarMapper
import com.newagewriter.processor.mapper.AbstractMapper
import java.awt.Color
import javax.swing.DebugGraphics

object MapperExample {
    @JvmStatic
    fun main(args: Array<String>) {
        val user = User(
            "user1",
            12345,
            UserInfo("http://avatar.url", 78f, System.currentTimeMillis()),
        )
        println("user: $user")

        val mapper = AbstractMapper.of(user)
        println("create mapper: $mapper")
        println("toMap: ${mapper?.toMap()}")
        println("toJson: ${mapper?.toJson()}")

        val plant = Plant("Sanseviera", PlantType.SUCCULENT, 0.5f, Color.GREEN)

        // Create mapper using factory
        val plantMapper = AbstractMapper.of(plant)

        println("toMap: ${plantMapper?.toMap()}")
        println("toJson: ${plantMapper?.toJson()}")

        // Create object from map
        val plantMap = plantMapper?.toMap() ?: emptyMap()
        val obj = AbstractMapper.toObject(Plant::class.java, plantMap)

        println("new plant object $obj")

        val t1: Any = 1

        val t2: Int = t1 as Int
        val car = Car("test", 123456789, 120, 30.5f, Color.GREEN, 5, 'A')

        val carMapper = CarMapper(car)

        println("json for car: ${carMapper.toJson()}")


        val carMap = mapOf<String, Any?>(
            "name" to "Ford",
            "id" to 84337682123,
            "size" to 120,
            "weight" to 45.6f,
            "color" to Color.CYAN.rgb,
            "version" to 20,
            "model" to 'A',
            "isManual" to true
        )

        val carMapper2 = CarMapper(null, carMap)

        val car2 = carMapper2.createMappedObj()

        println("Create car from map: ${car2.id} -> ${car2.name}")
        println("size: ${car2.size}")
        println("weight: ${car2.weight}")
        println("color: ${car2.color}")
        println("version: ${car2.version}")
        println("isManual: ${car2.isManual}")

        println("check device")

        val device1 = Device("Pixel", DeviceType.PHONE)

        val deviceMapper = AbstractMapper.of(device1)

        deviceMapper?.let {c ->
            println("json for device: ${c.toJson()}")
        } ?: throw NullPointerException("Cannot find mapper for device object")

    }
}