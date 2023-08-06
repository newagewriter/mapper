package com.newagewriter.example.model

import io.github.newagewriter.annotation.Field
import io.github.newagewriter.mapper.Mapper

@Mapper
data class FieldTestModel(
    @Field("_id")
    val id: String,
    val name: String
)
