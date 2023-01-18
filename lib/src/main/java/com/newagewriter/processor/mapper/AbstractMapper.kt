package com.newagewriter.processor.mapper

import com.newagewriter.processor.converter.ColorConverter
import com.newagewriter.processor.converter.DateConverter
import com.newagewriter.processor.converter.GenericConverter
import java.awt.Color
import java.io.InvalidClassException
import java.util.*

abstract class AbstractMapper<T>(
    protected var obj: T?,
    protected val objMap: Map<String, Any?>? = null
) {
    private val standardConverters: Map<String, GenericConverter<*, *>> = mapOf(
        Date::class.java.simpleName to DateConverter(),
        Color::class.java.simpleName to ColorConverter()

    )
    private var needRefresh = false

    constructor(objMap: Map<String, Any?>) : this(null, objMap)

    abstract fun toMap(): Map<String, Any?>

    protected abstract fun createMappedObj(): T

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

    fun getMappedObj(): T {
        if (obj == null || needRefresh) {
            obj = createMappedObj()
        }
        return obj ?: throw NullPointerException("Something went wrong during object creation")
    }

    fun<U> toType(type: Class<U>, element: Any?): U {
        if (element == null) {
            throw NullPointerException("Object is null")
        }
        if (type.isEnum) {
            val method = type.getMethod("valueOf", String::class.java)
            return method.invoke(null, element) as U
        }
        if (element is Map<*, *>) {
            return toObject(type, element as Map<String, Any?>)
                ?: throw InvalidClassException("Cannot cast $element to class ${type.simpleName}")
        }
        return throw InvalidClassException("Cannot cast $element to class ${type.simpleName}")
    }


    protected fun getValue(value: Any?): Any? {
        return value?.let { v ->
            getConverter(value.javaClass)?.toSimpleValue(v) ?: when (v) {
                is Int,
                is Float,
                is Double,
                is Long,
                is Short,
                is Boolean -> v
                is String -> "\"$v\""
                is Enum<*> -> "\"${v.name}\""
                null -> null
                else -> of(value)?.toJson() ?: "\"$value\""
            }
        }
    }

    protected fun createObject(classInfo: Class<T>): T {
        return objMap?.let { map ->
            var result: T? = null
            classInfo.constructors.forEach {
                val args = prepareArgs(map)
                result = it.newInstance(args) as T
            }
            result
        } ?: throw NullPointerException("To create object of ${classInfo.simpleName} map cannot be null")
    }

    protected fun<U> asType(cl: Class<U>, obj: Any?): U {
        return cl.cast(obj)
    }

    private fun prepareArgs(map: Map<String, Any?>): Array<Any?> {
        return map.map {
            it.value
        }.toTypedArray()
    }

    private fun prepareTypes(map: Map<String, Any?>): Array<Class<*>?> {
        return map.map {
            it.value?.javaClass
        }.toTypedArray()
    }

    private fun<T> getConverter(classInfo: Class<T>): GenericConverter<T, Any>? {
        return standardConverters[classInfo.simpleName] as GenericConverter<T, Any>?
    }

    companion object {
        lateinit var Factory: MapperFactory

        fun<T> of(value: T): AbstractMapper<T>? {
            // Load MapperUtils to provide correct mapper factory
            Class.forName("com.newagewriter.processor.mapper.MapperUtils")
            return Factory.of(value)
        }

        fun<T> toObject(objClass: Class<T>, map: Map<String, Any?>): T? {
            Class.forName("com.newagewriter.processor.mapper.MapperUtils")
            val mapper = Factory.forClass(objClass, map)
            return mapper?.getMappedObj()
        }
    }
}
