package com.newagewriter.model

import io.github.newagewriter.mapper.Mapper

@Mapper
class ModelWithStaticMethod(val id: String, val name: String) {

    companion object {
        fun getTestModel(): ModelWithStaticMethod {
            return ModelWithStaticMethod("test123", "test")
        }
    }
}
