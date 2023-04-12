package com.newagewriter

import io.github.newagewriter.mapper.Mapper
import io.github.newagewriter.processor.mapper.AbstractMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.awt.Color

@Mapper
data class ModelWithColor(
    val firstColor: Color,
    val secondColor: Color
)

class ColorFieldMapperTest {

    @Test
    fun testConvertModelToMap() {
        val model = ModelWithColor(Color.GREEN, Color.BLUE)
        val mapper = AbstractMapper.of(model)
        Assertions.assertNotEquals(null, mapper, "Mapper is null, cannot find mapper for given object.")

        val map = mapper?.toMap() ?: mapOf()

        Assertions.assertEquals(Color.GREEN.rgb, map["firstColor"], "Incorrect value for field: firstColor")
        Assertions.assertEquals(Color.BLUE.rgb, map["secondColor"], "Incorrect value for field: secondColor")
    }

    @Test
    fun testConvertMapToModel() {
        val map = mapOf(
            "firstColor" to Color.GREEN.rgb,
            "secondColor" to Color.BLUE.rgb
        )
        AbstractMapper.toObject(ModelWithColor::class.java, map)?.let { model ->

            Assertions.assertEquals(map["firstColor"], model.firstColor.rgb, "Incorrect value for field: firstColor")
            Assertions.assertEquals(map["secondColor"], model.secondColor.rgb, "Incorrect value for field: secondColor")
        } ?: Assertions.fail("Model is null, cannot convert map to model.")
    }
}
