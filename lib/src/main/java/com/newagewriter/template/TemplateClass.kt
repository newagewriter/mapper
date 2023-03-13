package com.newagewriter.template

import kotlin.text.StringBuilder

class TemplateClass(
    private val template: String
) {
    private val varMap: MutableMap<String, Any> = mutableMapOf()

    fun addVariable(name: String, value: Any): TemplateClass {
        varMap.put(name, value)
        return this
    }

    fun compile(): String {
        var result = template
        varMap.forEach { key, value ->
            if (value is List<*>) {
                val pattern = Regex("@foreach\\(\\\$$key(, separator=\"([^\"]+)\")?\\):([^@]+)@end")
                val matches = pattern.find(result)

                var mapBlock = StringBuilder()

                value.forEach { v ->
                    val separator = matches?.groupValues?.get(2) ?: ""
                    val statement = (matches?.groupValues?.get(3) ?: "").trimEnd()
                        .replace(Regex("\\\$element"), "$v")

                    mapBlock.append("$statement$separator")
                }
                println(mapBlock.toString())
                result = result.replaceFirst(pattern, mapBlock.toString())
            } else {
                val pattern = Regex("\\\$\\{$key}")
                println("regex pattern: ${pattern.pattern}")
                result = result.replace(pattern, value.toString())
            }
        }
        return result
    }
}