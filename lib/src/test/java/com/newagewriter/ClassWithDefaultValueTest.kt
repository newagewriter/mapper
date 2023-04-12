package com.newagewriter

import com.newagewriter.model.ClassWithDefaultBoolean
import com.newagewriter.model.ClassWithMultipleDefaultValue
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ClassWithDefaultValueTest {
    @Test
    fun testWhenDefaultValueIsInMap() {
        val map = mutableMapOf<String, Any?>(
            "id" to "876asd",
            "name" to "test1",
            "isActive" to false
        )
        val obj = AbstractMapper.toObject(ClassWithDefaultBoolean::class.java, map)

        Assertions.assertNotNull(obj, "Object is null")
        Assertions.assertEquals("876asd", obj?.id, "Incorrect value for id field")
        Assertions.assertEquals("test1", obj?.name, "Incorrect value for name field")
        Assertions.assertEquals(false, obj?.isActive, "Incorrect value for isActive field")
    }

    @Test
    fun testWhenDefaultValueIsNotInMap() {
        val map = mutableMapOf<String, Any?>(
            "id" to "876asd",
            "name" to "test1"
        )
        val obj = AbstractMapper.toObject(ClassWithDefaultBoolean::class.java, map)

        Assertions.assertNotNull(obj, "Object is null")
        Assertions.assertEquals("876asd", obj?.id, "Incorrect value for id field")
        Assertions.assertEquals("test1", obj?.name, "Incorrect value for name field")
        Assertions.assertEquals(true, obj?.isActive, "Incorrect value for isActive field")
    }

    @Test
    fun testMultipleDefaultInClass() {
        val allFieldMap = mutableMapOf<String, Any?>(
            "id" to "876asd",
            "name" to "test1",
            "firstDefault" to false,
            "secondDefault" to 0
        )
        val obj = AbstractMapper.toObject(ClassWithMultipleDefaultValue::class.java, allFieldMap)

        Assertions.assertNotNull(obj, "Object is null")
        Assertions.assertEquals("876asd", obj?.id, "Incorrect value for id field")
        Assertions.assertEquals("test1", obj?.name, "Incorrect value for name field")
        Assertions.assertEquals(false, obj?.firstDefault, "Incorrect value for firstDefault field")
        Assertions.assertEquals(0, obj?.secondDefault, "Incorrect value for secondDefault field")

        val firstDefaultMissingMap = mutableMapOf<String, Any?>(
            "id" to "876asd",
            "name" to "test1",
            "secondDefault" to 0
        )
        val obj2 = AbstractMapper.toObject(ClassWithMultipleDefaultValue::class.java, firstDefaultMissingMap)

        Assertions.assertNotNull(obj2, "Object is null")
        Assertions.assertEquals("876asd", obj2?.id, "Incorrect value for id field")
        Assertions.assertEquals("test1", obj2?.name, "Incorrect value for name field")
        Assertions.assertEquals(true, obj2?.firstDefault, "Incorrect value for firstDefault field")
        Assertions.assertEquals(0, obj2?.secondDefault, "Incorrect value for secondDefault field")

        val secondDefaultMissingMap = mutableMapOf<String, Any?>(
            "id" to "876asd",
            "name" to "test1",
            "firstDefault" to false
        )
        val obj3 = AbstractMapper.toObject(ClassWithMultipleDefaultValue::class.java, secondDefaultMissingMap)

        Assertions.assertNotNull(obj3, "Object is null")
        Assertions.assertEquals("876asd", obj3?.id, "Incorrect value for id field")
        Assertions.assertEquals("test1", obj3?.name, "Incorrect value for name field")
        Assertions.assertEquals(false, obj3?.firstDefault, "Incorrect value for firstDefault field")
        Assertions.assertEquals(1, obj3?.secondDefault, "Incorrect value for secondDefault field")

        val allDefaultsMissingMap = mutableMapOf<String, Any?>(
            "id" to "876asd",
            "name" to "test1"
        )
        val obj4 = AbstractMapper.toObject(ClassWithMultipleDefaultValue::class.java, allDefaultsMissingMap)

        Assertions.assertNotNull(obj4, "Object is null")
        Assertions.assertEquals("876asd", obj4?.id, "Incorrect value for id field")
        Assertions.assertEquals("test1", obj4?.name, "Incorrect value for name field")
        Assertions.assertEquals(true, obj4?.firstDefault, "Incorrect value for firstDefault field")
        Assertions.assertEquals(1, obj4?.secondDefault, "Incorrect value for secondDefault field")
    }
}
