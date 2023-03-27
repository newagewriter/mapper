package com.newagewriter.model

import com.newagewriter.processor.mapper.Mapper

@Mapper
data class ClassWithLongs(
    val l1: Long,
    val l2: Long
)
