package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelWithAdditionalField(
    val id: String,
    val name: String
) {
    var intField: Int? = null
}
