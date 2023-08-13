package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class ModelWithTwoConstructor(val id: String, val name: String) {
    constructor(id: String) : this(id, DEFAULT_NAME)
}

const val DEFAULT_NAME = "DEFAULT"
