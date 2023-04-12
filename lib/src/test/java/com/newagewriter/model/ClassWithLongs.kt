package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class ClassWithLongs(
    val l1: Long,
    val l2: Long
)
