package com.newagewriter.model

import com.newagewriter.processor.mapper.Mapper
import java.util.*

@Mapper
data class ClassWithDateType(
    val startDate: Date,
    val endDate: Date
)
