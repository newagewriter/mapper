package com.newagewriter.example.model

import com.newagewriter.processor.mapper.Mapper
import java.awt.Color

@Mapper
data class Car(
    val name: String,
    val id: Long,
    val size: Int,
    val weight: Float,
    val color: Color,
    val version: Short,
    val model: Char,
    val isManual: Boolean = true
)