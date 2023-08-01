package com.newagewriter.model

import io.github.newagewriter.annotation.Exclude
import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelWithExcludeVarField(
    val id: Int,
    val name: String
) {
    @Exclude
    var excludeField: Any? = null
}
