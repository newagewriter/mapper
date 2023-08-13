package com.newagewriter

import com.newagewriter.model.ModelWithAdditionalField
import com.newagewriter.model.ModelWithAdditionalLateInitField
import com.newagewriter.model.ModelWithoutAdditionalField
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class AdditionalFieldsTest {

    @Test
    fun testLoadFromMapWithoutAdditionalField() {
        Assertions.assertTrue(true)
        val map = mapOf(
            "id" to "abc123",
            "name" to "Test model",
            "intField" to 123
        )

        AbstractMapper.of(ModelWithoutAdditionalField::class.java)?.let { mapper ->
            val model = mapper.mapToModel(map)
            Assertions.assertNotNull(model)
        } ?: fail("Cannot create mapper for given type")
    }

    @Test
    fun testLoadFromMapWithAdditionalField() {
        Assertions.assertTrue(true)
        val map = mapOf(
            "id" to "abc123",
            "name" to "Test model",
            "intField" to 123
        )

        AbstractMapper.of(ModelWithAdditionalField::class.java)?.let { mapper ->
            val model = mapper.mapToModel(map)
            Assertions.assertNotNull(model)
            Assertions.assertNotNull(model.intField, "IntField wasn't load from map")
        } ?: fail("Cannot create mapper for given type")
    }

    @Test
    fun testLoadFromMapWithAdditionalLateInitField() {
        Assertions.assertTrue(true)
        val map = mapOf(
            "id" to "abc123",
            "name" to "Test model",
            "additionalField" to "Text for extra field"
        )

        AbstractMapper.of(ModelWithAdditionalLateInitField::class.java)?.let { mapper ->
            val model = mapper.mapToModel(map)
            Assertions.assertNotNull(model)
            Assertions.assertTrue(model.isAdditionalFieldInitialized(), "additionalField wasn't load from map")
        } ?: fail("Cannot create mapper for given type")
    }
}
