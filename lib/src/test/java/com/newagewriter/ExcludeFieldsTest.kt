package com.newagewriter

import com.newagewriter.model.ModelWithExcludeLateInitField
import com.newagewriter.model.ModelWithExcludeVarField
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class ExcludeFieldsTest {
    @Test
    fun testModelWithExcludeVarField() {
        AbstractMapper.of(ModelWithExcludeVarField::class.java)?.let { mapper ->
            val model = ModelWithExcludeVarField(12, "test")
            model.excludeField = "test element"
            val map = mapper.toMap(model)
            Assertions.assertFalse(map.containsKey("excludeField"))
        }
    }

    @Test
    fun testModelWithExcludeLateinitField() {
        AbstractMapper.of(ModelWithExcludeLateInitField::class.java)?.let { mapper ->
            val model = ModelWithExcludeLateInitField(12, "test")
            model.excludeField = "test element"
            val map = mapper.toMap(model)
            Assertions.assertFalse(map.containsKey("excludeField"))
        } ?: fail("Cannot get mapper for given class")

    }
}
