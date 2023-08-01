package com.newagewriter

import com.newagewriter.model.ModelWithExcludeLateInitField
import com.newagewriter.model.ModelWithExcludeVarField
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ExcludeFieldsTest {
    @Test
    fun testModelWithExcludeVarField() {
        val model = ModelWithExcludeVarField(12, "test")
        model.excludeField = "test element"
        val map = AbstractMapper.ofOrThrow(model).toMap()
        Assertions.assertFalse(map.containsKey("excludeField"))
    }

    @Test
    fun testModelWithExcludeLateinitField() {
        val model = ModelWithExcludeLateInitField(12, "test")
        model.excludeField = "test element"
        val map = AbstractMapper.ofOrThrow(model).toMap()
        Assertions.assertFalse(map.containsKey("excludeField"))
    }
}
