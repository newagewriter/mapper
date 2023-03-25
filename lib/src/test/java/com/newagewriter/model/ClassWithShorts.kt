package com.newagewriter.model

import com.newagewriter.processor.mapper.Mapper

@Mapper
data class ClassWithShorts(
    val s1: Short,
    val s2: Short
)