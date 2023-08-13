package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelWithAdditionalLateInitField(
    val id: String,
    val name: String
) {
    lateinit var additionalField: String

    fun isAdditionalFieldInitialized(): Boolean {
        return this::additionalField.isInitialized
    }
}
