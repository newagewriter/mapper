package com.newagewriter

import com.newagewriter.model.ObjectWithEnum
import com.newagewriter.model.SomeEnum
import com.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ObjectWithEnumTest {
    @Test
    fun testConversionFromObjectToMapForEnum() {
        val someObject = ObjectWithEnum(1, SomeEnum.FIRST_OPTION)

        val someMapper = AbstractMapper.of(someObject)

        Assertions.assertNotEquals(null, someMapper, "Mapper is null, cannot find mapper for given object.")

        someMapper?.let { m ->
            val someMap = m.toMap()

            Assertions.assertEquals(SomeEnum.FIRST_OPTION.name, someMap["enumField"], "enumField incorrect value")
        }
    }

    @Test
    fun testConversionFromMapToObjectForEnum() {
        val someMap = mapOf<String, Any?>(
            "someValue" to 1,
            "enumField" to "FIRST_OPTION"
        )

        val someObject = AbstractMapper.toObject(ObjectWithEnum::class.java, someMap)

        Assertions.assertNotEquals(null, someObject, "Object is null, cannot convert map to object.")

        someObject?.let { so ->
            Assertions.assertEquals(SomeEnum.FIRST_OPTION, so.enumField, "enumField incorrect value")
        }
    }
}
