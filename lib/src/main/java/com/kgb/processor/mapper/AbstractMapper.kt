package com.kgb.processor.mapper

abstract class AbstractMapper<T>(protected val mappedObj: T) {
    abstract fun toMap(): Map<String, Any?>

    fun toJson(): String {
        val result = toMap()
        val builder = StringBuilder()
        builder.appendLine("{")
        var first = true
        result.forEach { entry ->
            if (!first) builder.appendLine(",")

            builder.append("    \"${entry.key}\" : ${getValue(entry.value)}")
            first = false
        }
        builder.appendLine("\n}")
        return builder.toString()
    }


    protected fun getValue(value: Any?): Any? {
        return when (value) {
            is Int,
            is Float,
            is Double,
            is Long,
            is Short,
            is Boolean -> value
            is String -> "$value"
            is Enum<*> -> value.name
            null -> null
            else -> of(value)?.toJson() ?: "$value"
        }
    }

    private fun<T> of(value: T): AbstractMapper<T>? {
        Class.forName("com.kgb.processor.mapper.MapperUtils")
        return Factory.of(value)
    }

    companion object {
        lateinit var Factory: MapperFactory
    }
}