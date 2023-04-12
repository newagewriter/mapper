package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper
import java.util.*

@Mapper
data class ClassWithDateType(
    val startDate: Date,
    val endDate: Date
)
