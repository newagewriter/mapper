package com.newagewriter

import com.newagewriter.template.TemplateLoader

fun main(args: Array<String>) {
    println("prepare template")
    val myMap = listOf(
        "id",
        "name"
    )
    println(TemplateLoader
        .load("MapperTemplate")
        .addVariable("className", "Car")
        .addVariable("fields", myMap)
        .compile()
    )
}