package com.newagewriter

import com.newagewriter.model.ModelWithStaticField
import com.newagewriter.model.ModelWithStaticMethod
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class ModelWithCompanionObjectTest {

    @Test
    fun checkModelWithSomeStaticMethodsTest() {
        AbstractMapper.of(ModelWithStaticMethod::class.java)?.let { mapper ->
            mapper.mapToModel(mapOf("id" to "123", "name" to "test"))
            Assertions.assertTrue(true)
        } ?: fail("Cannot create mapper for: ${ModelWithStaticMethod::class.java}")
    }

    @Test
    fun checkModelWithSomeStaticFieldsTest() {
        AbstractMapper.of(ModelWithStaticField::class.java)?.let { mapper ->
            mapper.mapToModel(mapOf("id" to "123", "name" to "test"))
            Assertions.assertTrue(true)
        } ?: fail("Cannot create mapper for: ${ModelWithStaticField::class.java}")
    }
}
