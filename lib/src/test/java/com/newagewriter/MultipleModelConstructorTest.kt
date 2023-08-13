package com.newagewriter

import com.newagewriter.model.DEFAULT_NAME
import com.newagewriter.model.ModelWithTwoConstructor
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultipleModelConstructorTest {

    @Test
    fun useConstructorWithOneParamTest() {
        val map = mapOf(
            "id" to "abc123"
        )

        AbstractMapper.of(ModelWithTwoConstructor::class.java)?.let { m ->
            val model = m.mapToModel(map)
            Assertions.assertEquals("abc123", model.id, "wrong value for id")
            Assertions.assertEquals(DEFAULT_NAME, model.name, "wrong value for name")
        }
    }

    @Test
    fun useSecondConstructorWithTwoArgumentsTest() {
        val map = mapOf(
            "id" to "abc123",
            "name" to "Test name"
        )

        AbstractMapper.of(ModelWithTwoConstructor::class.java)?.let { m ->
            val model = m.mapToModel(map)
            Assertions.assertEquals("abc123", model.id, "wrong value for id")
            Assertions.assertEquals("Test name", model.name, "wrong value for name")
        }
    }
}
