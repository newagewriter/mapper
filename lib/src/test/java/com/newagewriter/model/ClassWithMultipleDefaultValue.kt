package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class ClassWithMultipleDefaultValue(
    val id: String,
    val name: String,
    val firstDefault: Boolean = true,
    val secondDefault: Int = 1
)
