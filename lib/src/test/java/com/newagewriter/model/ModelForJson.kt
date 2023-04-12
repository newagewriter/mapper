package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelForJson(
    val testInt: Int,
    val testShort: Short,
    val testByte: Byte,
    val testLong: Long,
    val testFloat: Float,
    val testDouble: Double,
    val testChar: Char,
    val testString: String
)
