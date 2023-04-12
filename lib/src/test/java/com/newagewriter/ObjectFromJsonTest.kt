package com.newagewriter

import com.newagewriter.model.ModelForJson
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ObjectFromJsonTest {
    @Test
    fun testJsonParseForPrimitiveType() {
        val jsonString = """
        {
            "testInt" : 1, 
            "testShort" : 4,
            "testByte" : 12,
            "testLong" : 1234567890,
            "testFloat" : 54.3456,
            "testDouble" : 0.1234567890,    
            "testChar" : "A",
            "testString" : "hello from json",
        }
        """
        val model = AbstractMapper.fromJsonToObject(ModelForJson::class.java, jsonString)
        Assertions.assertNotNull(model, "Cannot convert json to model")
        Assertions.assertEquals(1, model?.testInt, "Wrong conversion for testInt")
        Assertions.assertEquals(4, model?.testShort, "Wrong conversion for testShort")
        Assertions.assertEquals(12, model?.testByte, "Wrong conversion for testByte")
        Assertions.assertEquals(1234567890, model?.testLong, "Wrong conversion for testLong")
        Assertions.assertEquals(54.3456f, model?.testFloat, "Wrong conversion for testFloat")
        Assertions.assertEquals(0.1234567890, model?.testDouble, "Wrong conversion for testDouble")
        Assertions.assertEquals('A', model?.testChar, "Wrong conversion for testChar")
        Assertions.assertEquals("hello from json", model?.testString, "Wrong conversion for testString")
    }

    @Test
    fun testJsonParsingArrayOfObject() {
        val arrayJsonString = """[
        {
            "testInt" : 1, 
            "testShort" : 4,
            "testByte" : 12,
            "testLong" : 1234567890,
            "testFloat" : 54.3456,
            "testDouble" : 0.1234567890,    
            "testChar" : "A",
            "testString" : "hello from json",
        },
        {
            "testInt" : 2, 
            "testShort" : 8,
            "testByte" : 13,
            "testLong" : 91234567890,
            "testFloat" : 154.3456,
            "testDouble" : 1.0987654321,    
            "testChar" : "B",
            "testString" : "hello from json second test",
        }
        ]
        """

        val modelList = AbstractMapper.fromJsonToArray(ModelForJson::class.java, arrayJsonString)
        Assertions.assertEquals(2, modelList.size, "Wrong conversion to array of object. Array must contains 2 object")

        val model1 = modelList[0]
        Assertions.assertNotNull(model1, "Cannot convert json to model")
        Assertions.assertEquals(1, model1?.testInt, "Wrong conversion for testInt")
        Assertions.assertEquals(4, model1?.testShort, "Wrong conversion for testShort")
        Assertions.assertEquals(12, model1?.testByte, "Wrong conversion for testByte")
        Assertions.assertEquals(1234567890, model1?.testLong, "Wrong conversion for testLong")
        Assertions.assertEquals(54.3456f, model1?.testFloat, "Wrong conversion for testFloat")
        Assertions.assertEquals(0.1234567890, model1?.testDouble, "Wrong conversion for testDouble")
        Assertions.assertEquals('A', model1?.testChar, "Wrong conversion for testChar")
        Assertions.assertEquals("hello from json", model1?.testString, "Wrong conversion for testString")

        val model2 = modelList[1]
        Assertions.assertNotNull(model2, "Cannot convert json to model")
        Assertions.assertEquals(2, model2?.testInt, "Wrong conversion for testInt")
        Assertions.assertEquals(8, model2?.testShort, "Wrong conversion for testShort")
        Assertions.assertEquals(13, model2?.testByte, "Wrong conversion for testByte")
        Assertions.assertEquals(91234567890, model2?.testLong, "Wrong conversion for testLong")
        Assertions.assertEquals(154.3456f, model2?.testFloat, "Wrong conversion for testFloat")
        Assertions.assertEquals(1.0987654321, model2?.testDouble, "Wrong conversion for testDouble")
        Assertions.assertEquals('B', model2?.testChar, "Wrong conversion for testChar")
        Assertions.assertEquals("hello from json second test", model2?.testString, "Wrong conversion for testString")
    }
}
