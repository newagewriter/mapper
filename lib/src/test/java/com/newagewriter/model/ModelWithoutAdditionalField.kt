package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelWithoutAdditionalField(
    val id: String,
    val name: String
)
