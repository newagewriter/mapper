package com.newagewriter.example.model

import com.newagewriter.processor.mapper.Mapper
import java.awt.Color

//@Mapper
data class Plant(
    val name: String,
    val type: PlantType,
    val maxHeight: Float,
    val color: Color
)