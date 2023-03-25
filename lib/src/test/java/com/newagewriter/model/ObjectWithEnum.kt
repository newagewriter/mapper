package com.newagewriter.model

import com.newagewriter.processor.mapper.Mapper

enum class SomeEnum {
    FIRST_OPTION,
    SECOND_OPTION
}

@Mapper
data class ObjectWithEnum(
    val someValue: Int,
    val enumField: SomeEnum
)