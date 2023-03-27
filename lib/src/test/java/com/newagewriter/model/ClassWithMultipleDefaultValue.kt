package com.newagewriter.model

import com.newagewriter.processor.mapper.Mapper

@Mapper
data class ClassWithMultipleDefaultValue(
    val id: String,
    val name: String,
    val firstDefault: Boolean = true,
    val secondDefault: Int = 1
)
