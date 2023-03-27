package com.newagewriter

import com.newagewriter.model.ClassWithInts
import com.newagewriter.model.ClassWithLongs
import com.newagewriter.model.ClassWithShorts
import com.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PrimitiveFieldMapperTest {
    @Test
    fun testMapperWithIntFields() {
        val model = ClassWithInts(1, 2)
        // check if mapper exist
        val mapper = AbstractMapper.of(model)

        Assertions.assertNotEquals(null, mapper, "Mapper is null, cannot find mapper for given object")

        val modelMap = mapper?.toMap() ?: mapOf()
        Assertions.assertEquals(1, modelMap["i1"], "Wrong conversion for field: i1")
        Assertions.assertEquals(2, modelMap["i2"], "Wrong conversion for field: i2")
    }

    @Test
    fun testMapperWithLongFields() {
        val model = ClassWithLongs(1, 2)
        // check if mapper exist
        val mapper = AbstractMapper.of(model)

        Assertions.assertNotEquals(null, mapper, "Mapper is null, cannot find mapper for given object")

        val modelMap = mapper?.toMap() ?: mapOf()
        Assertions.assertEquals(1L, modelMap["l1"], "Wrong conversion from object to map for key: l1")
        Assertions.assertEquals(2L, modelMap["l2"], "Wrong conversion from object to map for field: l2")

        val map = mapOf<String, Any?>(
            "l1" to 23,
            "l2" to 45L
        )

        val modelFromMap = AbstractMapper.toObject(ClassWithLongs::class.java, map)

        Assertions.assertNotEquals(null, modelFromMap, "Object is null, conversion failed")

        Assertions.assertEquals(23L, modelFromMap?.l1, "Wrong conversion from map to object for field: l1")
        Assertions.assertEquals(45L, modelFromMap?.l2, "Wrong conversion from map to object for field: l2")
    }

    @Test
    fun testMapperWithShortFields() {
        val model = ClassWithShorts(1, 2)
        // check if mapper exist
        val mapper = AbstractMapper.of(model)

        Assertions.assertNotEquals(null, mapper, "Mapper is null, cannot find mapper for given object")

        val modelMap = mapper?.toMap() ?: mapOf()
        Assertions.assertEquals(1.toShort(), modelMap["s1"], "Wrong conversion from object to map for key: s1")
        Assertions.assertEquals(2.toShort(), modelMap["s2"], "Wrong conversion from object to map for key: s2")

        val map = mapOf<String, Any?>(
            "s1" to 23,
            "s2" to 45
        )

        val modelFromMap = AbstractMapper.toObject(ClassWithShorts::class.java, map)

        Assertions.assertNotEquals(null, modelFromMap, "Object is null, conversion failed")

        Assertions.assertEquals(23.toShort(), modelFromMap?.s1, "Wrong conversion from map to object for field: s1")
        Assertions.assertEquals(45.toShort(), modelFromMap?.s2, "Wrong conversion from map to object for field: s2")
    }
}
