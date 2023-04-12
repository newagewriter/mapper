package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class ClassWithDefaultBoolean(
    val id: String,
    val name: String,
    val isActive: Boolean = true
)
