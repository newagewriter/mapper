package com.newagewriter

import com.newagewriter.template.TemplateLoader

fun main(args: Array<String>) {
    println("prepare template")
    val fields = listOf(
        "id",
        "name"
    )
    val myMap = mapOf<String, Any?>(
        "id" to "Long",
        "name" to "String"
    )
    println(TemplateLoader
        .load("MapperTemplate")
        .addVariable("classPackage", "com.naw.model")
        .addVariable("className", "Car")
        .addVariable("fields", fields)
        .addVariable("map", myMap)
        .compile()
    )
}