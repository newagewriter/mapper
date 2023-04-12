package com.newagewriter.example.model

import io.github.newagewriter.mapper.Mapper
import java.awt.Color

@Mapper
data class Plant(
    val name: String,
    val type: PlantType,
    val maxHeight: Float,
    val color: Color
)
