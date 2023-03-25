package com.newagewriter.processor.mapper

import com.newagewriter.processor.converter.ColorConverter
import com.newagewriter.processor.converter.DateConverter
import com.newagewriter.processor.converter.GenericConverter
import java.awt.Color
import java.io.InvalidClassException
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure


abstract class AbstractMapper<T>(
    protected var obj: T?,
    protected val objMap: Map<String, Any?>? = null
) where T : Any {
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
                else -> of(value)?.toJson() ?: "\"$value\""
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
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

    @Suppress("UNCHECKED_CAST")
    private fun<T> getConverter(classInfo: Class<T>): GenericConverter<T, Any>? {
        return convertersList[classInfo.simpleName] as GenericConverter<T, Any>?
    }

    protected fun createFromMap(map: Map<String, Any?>, clazz: KClass<T>): T {

        clazz.constructors.forEach {c ->
            println("check constructors: ${c.name}")
            val params = mutableMapOf<String, KParameter>()
            c.parameters.forEach { p ->
                if (!p.isOptional || map.containsKey(p.name)) {
                    params[p.name ?: "<UNKNOWN>"] = p
                }
            }
            if (map.keys.containsAll(params.keys) && map.keys.size == params.size ) {
                val args = map.mapKeys { p ->
                    params[p.key]!!
                }
                try {
                    println("args: $args")
                    return c.callBy(args)
                } catch (e: IllegalArgumentException) {
                    println("exception: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        throw InvocationTargetException(Exception("Cannot find constructor for given map: $map"))
    }

    companion object {
        lateinit var Factory: MapperFactory
        private val convertersList: MutableMap<String, GenericConverter<*, *>> = HashMap()
        init {
            val converter = Class.forName("com.newagewriter.processor.converter.ConverterUtils")
            val initConverters = converter.getMethod("initConverters")
            prepareConverters(
                mapOf(
                    Date::class.java.simpleName to DateConverter(),
                    Color::class.java.simpleName to ColorConverter()
                )
            )

            prepareConverters( initConverters.invoke(null) as? Map<String, GenericConverter<*, *>>)

        }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun<U> toType(type: Class<U>, element: Any?): U where U : Any {
            return if (element == null) {
                throw NullPointerException("Object is null")
            } else if (isPrimitive(type)) {
                castPrimitiveTo(element, type)
            } else if (convertersList.containsKey(type.simpleName)) {
                val value: GenericConverter<U, Any> = convertersList[type.simpleName] as GenericConverter<U, Any>
                value.toEntity(element)
            } else if (type.isEnum) {
                val method = type.getMethod("valueOf", String::class.java)
                method.invoke(null, element) as U
            } else if (element is Map<*, *>) {
                toObject(type, element as Map<String, Any?>)
                    ?: throw InvalidClassException("Cannot cast $element to class ${type.simpleName}")
            } else {
                throw InvalidClassException("Cannot cast $element to class ${type.simpleName}")
            }
        }

        @JvmStatic
        fun prepareConverters(converters: Map<String, GenericConverter<*, *>>?) {
            converters?.let {
                convertersList.putAll(converters)
            }

        }

        @JvmStatic
        fun<T> of(value: T): AbstractMapper<T>? where T : Any {
            // Load MapperUtils to provide correct mapper factory
            Class.forName("com.newagewriter.processor.mapper.MapperUtils")
            return Factory.of(value)
        }

        @JvmStatic
        fun<T> toObject(objClass: Class<T>, map: Map<String, Any?>): T? where T : Any {
            Class.forName("com.newagewriter.processor.mapper.MapperUtils")
            val mapper = Factory.forClass(objClass, map)
            return mapper?.getMappedObj()
        }

        fun getConverterForType(type: Class<*>): String? {
            return convertersList[type.simpleName]?.javaClass?.toGenericString()
        }

        @JvmStatic
        protected fun isPrimitive(type: Class<*>): Boolean {
            return when(type.simpleName) {
                String::class.java.simpleName,
                Char::class.java.simpleName,
                Boolean::class.java.simpleName,
                Byte::class.java.simpleName,
                Short::class.java.simpleName,
                Int::class.java.simpleName,
                Long::class.java.simpleName,
                Float::class.java.simpleName,
                Double::class.java.simpleName -> true
                else -> false
            }
        }

        private fun<U> castPrimitiveTo(element: Any, type: Class<U>): U {
            return when(type.simpleName) {
                Byte::class.java.simpleName -> (element as Int).toByte()
                Short::class.java.simpleName -> (element as Int).toShort()
                else -> element
            } as U
        }
    }
}
