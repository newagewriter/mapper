package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

enum class SomeEnum {
    FIRST_OPTION,
    SECOND_OPTION
}

@Mapper
data class ObjectWithEnum(
    val someValue: Int,
    val enumField: SomeEnum
)
