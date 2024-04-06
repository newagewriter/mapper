package com.newagewriter.example

import ModelWithNoPackage
import io.github.newagewriter.processor.mapper.AbstractMapper

fun main(args: Array<String>) {
    println("Test model without pacakge")
    val model = ModelWithNoPackage("123", "test")
    AbstractMapper.of(ModelWithNoPackage::class.java)?.let { mapper ->
        val jsonModel = mapper.toJson(model)
        println("json: $jsonModel")
    }
}
