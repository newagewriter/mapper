package com.newagewriter.model

import io.github.newagewriter.annotation.Field
import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelWithOneAnnotatedField(
    @Field("_id")
    val id: String,
    val name: String,
    val value: Double
)
