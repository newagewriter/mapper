package com.newagewriter

import com.newagewriter.model.ClassWithInts
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
}