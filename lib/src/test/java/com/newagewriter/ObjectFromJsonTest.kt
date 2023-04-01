package com.newagewriter

import com.newagewriter.json.wrapper.JsonWrapper
import com.newagewriter.model.ModelForJson
import com.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ObjectFromJsonTest {
    @Test
    fun testJsonParsePrimitiveType() {
        val jsonString = "{\"test\" : 1 }"
        val model = AbstractMapper.fromJsonToObject(ModelForJson::class.java, jsonString)
        Assertions.assertNotNull(model, "Cannot convert json to model")
        Assertions.assertEquals(1, model?.test, "Wrong conversion")
    }
}
