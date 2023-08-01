package com.newagewriter.model

import io.github.newagewriter.annotation.Exclude
import io.github.newagewriter.annotation.Field
import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelWithExcludeLateInitField(
    @Field("id")
    val id: Int,
    val name: String
) {
    @Exclude
    lateinit var excludeField: Any
}
