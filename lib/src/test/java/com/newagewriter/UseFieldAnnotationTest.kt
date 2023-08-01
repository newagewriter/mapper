package com.newagewriter

import com.newagewriter.model.ModelWithMultipleAnnotatedFields
import com.newagewriter.model.ModelWithOneAnnotatedField
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class UseFieldAnnotationTest {

    @Test
    fun testConvertModelToMapWhenOneFieldIsAnnotated() {
        val id = "AC12346"
        val name = "test1"
        val value = 4.5
        val model = ModelWithOneAnnotatedField(id, name, value)
        AbstractMapper.of(model)?.let { mapper ->
            val toMap = mapper.toMap()
            // check if map contains key _id (not id)
            Assertions.assertTrue(toMap.containsKey("_id"))
            Assertions.assertEquals(toMap["_id"], id)

            Assertions.assertEquals(toMap["name"], name)
            Assertions.assertEquals(toMap["value"], value)
        } ?: fail("Cannot find mapper for given object")
    }

    @Test
    fun testConvertMapToModelWhenOneFieldIsAnnotated() {
        val id = "AC12346"
        val name = "test1"
        val value = 4.5
        val fromMap = mapOf<String, Any?>(
            "_id" to id,
            "name" to name,
            "value" to value
        )
        AbstractMapper.toObject(ModelWithOneAnnotatedField::class.java, fromMap)?.let { model ->

            Assertions.assertEquals(model.id, id)
            Assertions.assertEquals(model.name, name)
            Assertions.assertEquals(model.value, value)
        } ?: fail("Cannot convert map to model")
    }

    @Test
    fun testConvertToMapMultipleAnnotatedFields() {
        val id = "AC12346"
        val name = "test1"
        val value = 4.5
        val model = ModelWithMultipleAnnotatedFields(id, name, value)
        AbstractMapper.of(model)?.let { mapper ->
            val toMap = mapper.toMap()
            // check if map contains key _id (not id)
            Assertions.assertTrue(toMap.containsKey("_id"))
            Assertions.assertEquals(toMap["_id"], id)

            Assertions.assertTrue(toMap.containsKey("model_name"))
            Assertions.assertEquals(toMap["model_name"], name)

            Assertions.assertTrue(toMap.containsKey("double_value"))
            Assertions.assertEquals(toMap["double_value"], value)
        } ?: fail("Cannot find mapper for given object")
    }

    @Test
    fun testConvertMapToModelMultipleAnnotatedFields() {
        val id = "AC12346"
        val name = "test1"
        val value = 4.5
        val fromMap = mapOf<String, Any?>(
            "_id" to id,
            "model_name" to name,
            "double_value" to value
        )
        AbstractMapper.toObject(ModelWithMultipleAnnotatedFields::class.java, fromMap)?.let { model ->
            Assertions.assertEquals(model.id, id)
            Assertions.assertEquals(model.name, name)
            Assertions.assertEquals(model.value, value)
        } ?: fail("Cannot convert map to model")
    }
}
