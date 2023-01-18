package com.newagewriter.processor.converter

import java.awt.Color

class ColorConverter : GenericConverter<Color, Int> {
    override fun toSimpleValue(entity: Color): Int = entity.rgb

    override fun toEntity(value: Int): Color {
        return Color(value)
    }
}