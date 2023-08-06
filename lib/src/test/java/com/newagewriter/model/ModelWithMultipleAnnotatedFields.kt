package com.newagewriter.model

import io.github.newagewriter.annotation.Field
import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelWithMultipleAnnotatedFields(
    @Field("_id")
    val id: String,
    @Field("model_name")
    val name: String,
    @Field("double_value")
    val value: Double
)
