package com.newagewriter.model

import com.newagewriter.processor.mapper.Mapper

@Mapper
data class ClassWithDefaultBoolean(
    val id: String,
    val name: String,
    val isActive: Boolean = true
)
