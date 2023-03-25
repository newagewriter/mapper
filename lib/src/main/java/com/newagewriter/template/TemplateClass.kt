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
                var matches = pattern.find(result)
                while(matches != null) {
                    val mapBlock = StringBuilder()
                    value.forEach { v ->
                        val separator = matches?.groupValues?.get(2) ?: ""
                        val statement = (matches?.groupValues?.get(3) ?: "").trimEnd()
                            .replace(Regex("\\\$element"), "$v")

                        mapBlock.append("$statement$separator")
                    }
//                    println(mapBlock.toString())
                    result = result.replaceFirst(pattern, mapBlock.toString())
                    matches = matches.next()
                }

            } else if (value is Map<*, *>) {
                val pattern = Regex("@foreach\\(\\\$$key as ([a-zA-Z0-1]+)\\s*->\\s*([a-zA-Z0-1]+)(, separator=\"([^\"]+)\")?\\):([^@]+)@end")
                var matches = pattern.find(result)
//                var index = 0
//                matches?.groups?.forEach {
//                    println("group ${index++}: ${it?.value}")
//                }
                while(matches != null) {
                    val mapBlock = StringBuilder()
                    value.forEach { k, v ->
                        val keyName = matches?.groupValues?.get(1)
                        val valueName = matches?.groupValues?.get(2)
                        val separator = matches?.groupValues?.get(4) ?: ""
                        val statement = (matches?.groupValues?.get(5) ?: "").trimEnd()
                            .replace(Regex("\\\$$keyName"), "$k")
                            .replace(Regex("\\\$$valueName"), "$v")

                        mapBlock.append("$statement$separator")
                    }
                    println(mapBlock.toString())
                    result = result.replaceFirst(pattern, mapBlock.toString())
                    matches = matches.next()
                }

            } else {
                val pattern = Regex("\\\$\\{$key}")
                println("regex pattern: ${pattern.pattern}")
                result = result.replace(pattern, value.toString())
            }
        }
        return result
    }
}