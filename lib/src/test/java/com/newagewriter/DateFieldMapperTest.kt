package com.newagewriter

import com.newagewriter.model.ClassWithDateType
import com.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class DateFieldMapperTest {

    @Test
    fun testConvertObjectWithDateToMap() {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + 1000
        val model = ClassWithDateType(Date(startTime), Date(endTime))

        val mapper = AbstractMapper.of(model)
        Assertions.assertNotEquals(null, mapper, "Mapper is null, cannot find mapper for given object.")

        val modelMap = mapper?.toMap() ?: mapOf()

        Assertions.assertEquals(true, modelMap.containsKey("startDate"), "Map doesn't contains startDate")
        Assertions.assertEquals(startTime, modelMap["startDate"], "StartDate incorrect value")
        Assertions.assertEquals(true, modelMap.containsKey("endDate"), "Map doesn't contains endDate")
        Assertions.assertEquals(endTime, modelMap["endDate"], "EndDate incorrect value")
    }

    @Test
    fun testConvertValueToDateUsingMapper() {
        val myDate = Date()
        val startTime = myDate.time
        val endTime = myDate.time + 1000
        val modelMap = mapOf<String, Any?>(
            "startDate" to startTime,
            "endDate" to endTime
        )

        val model = AbstractMapper.toObject(ClassWithDateType::class.java, modelMap)

        Assertions.assertNotEquals(null, model, "Cannot convert map: $modelMap to ClassWithDateType object")
        Assertions.assertEquals(startTime, model?.startDate?.time, "Incorect value for startTime")
        Assertions.assertEquals(endTime, model?.endDate?.time, "Incorect value for endTime")
    }

    @Test
    fun testConvertObjectToJson() {
        val myDate = Date()
        val startTime = myDate.time
        val endTime = myDate.time + 1000
        val model = ClassWithDateType(Date(startTime), Date(endTime))

        val mapper = AbstractMapper.of(model)
        Assertions.assertNotEquals(null, mapper, "Mapper is null, cannot find mapper for given object.")

        val jsonString = mapper?.toJson() ?: ""

        Assertions.assertTrue {
            val pattern = "\"startDate\" : $startTime"
            jsonString.contains(pattern)
        }

        Assertions.assertTrue {
            val pattern = "\"endDate\" : $endTime"
            jsonString.contains(pattern)
        }
    }
}
