package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
class ModelWithStaticField(val id: String, val name: String) {

    companion object {
        const val SOME_VALUE = "some_value"
    }
}
