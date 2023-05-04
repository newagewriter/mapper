package io.github.newagewriter.processor.mapper

import io.github.newagewriter.json.wrapper.JsonWrapper
import io.github.newagewriter.processor.converter.DateConverter
import io.github.newagewriter.processor.converter.GenericConverter
import io.github.newagewriter.processor.converter.PrimitiveConverter
import java.io.InputStream
import java.io.InvalidClassException
import java.lang.reflect.InvocationTargetException
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Base class for all generated mapper. Contains methods that are common for every converter
 * Class contains only one protected constructor to initialize class with object or with map
 * Class that extend [AbstractMapper] must implement two methods:
 * # [toMap] - : method convert internal object to map
 * # [createMappedObj] - create object from internal map
 */
abstract class AbstractMapper<T> protected constructor(
    protected var obj: T?,
    protected val objMap: Map<String, Any?>? = null
) where T : Any {
    private var needRefresh = false

    /**
     * Method convert object [obj] to Map
     * @return map with all fields from object [obj]
     */
    abstract fun toMap(): Map<String, Any?>

    /**
     * Method convert map to object
     * @return
     */
    protected abstract fun createMappedObj(): T

    /**
     * Method convert object of [T] class to json
     * @return Json string for object [obj]
     */
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

    /**
     * Method get object of [T] class. If [obj] fields is null or need refresh
     * call [createMappedObj] to create object before return
     * @return object of [T] class
     */
    fun getMappedObj(): T {
        if (obj == null || needRefresh) {
            obj = createMappedObj()
        }
        return obj ?: throw NullPointerException("Something went wrong during object creation")
    }

    protected fun getValue(value: Any?): Any? {
        return value?.let { v ->
            getConverter(v.javaClass)?.toSimpleValue(v) ?: when (v) {
                is Int,
                is Float,
                is Double,
                is Long,
                is Short,
                is Boolean -> v
                is String -> "$v"
                is Enum<*> -> v.name
                else -> of(v)?.toJson() ?: "$v"
            }
        }
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
    private fun<T> getConverter(classInfo: Class<T>): GenericConverter<T, Comparable<Any>>? {
        return convertersList[classInfo.simpleName] as GenericConverter<T, Comparable<Any>>?
    }

    protected fun createFromMap(map: Map<String, Any?>, clazz: KClass<T>): T {
        clazz.constructors.forEach { c ->
            val params = mutableMapOf<String, KParameter>()
            c.parameters.forEach { p ->
                if (!p.isOptional || map.containsKey(p.name)) {
                    params[p.name ?: "<UNKNOWN>"] = p
                }
            }
            if (map.keys.containsAll(params.keys) && map.keys.size == params.size) {
                try {
                    return c.callBy(
                        map.mapKeys { p ->
                            params[p.key]!!
                        }
                    )
                } catch (e: IllegalArgumentException) {
                    println("exception: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        throw InvocationTargetException(Exception("Cannot find constructor for given map: $map"))
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        lateinit var Factory: MapperFactory
        private val convertersList: MutableMap<String, GenericConverter<*, *>> = HashMap()

        init {
            val converter = Class.forName("io.github.newagewriter.processor.converter.ConverterUtils")
            val initConverters = converter.getMethod("initConverters")
            val map = mutableMapOf<String, GenericConverter<*, *>>(
                Date::class.java.simpleName to DateConverter()
            )

            getColorJavaConverters()?.let {
                map[it.first] = it.second
            }
            prepareConverters(map)

            prepareConverters(initConverters.invoke(null) as? Map<String, GenericConverter<*, *>>)
        }

        /**
         * Static
         */
        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        protected fun<U> toType(type: Class<U>, element: Any?): U where U : Any {
            return if (element == null) {
                throw NullPointerException("Object is null")
            } else if (PrimitiveConverter.isPrimitive(type)) {
                PrimitiveConverter.castPrimitiveTo(element, type)
            } else if (element is Comparable<*> && convertersList.containsKey(type.simpleName)) {
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

        /**
         * Method get proper mapper instance for given object
         * @param value - object used to find proper mapper
         * @return mapper for given object or null if mapper for it doesn't exist
         */
        @JvmStatic
        fun<T> of(value: T): AbstractMapper<T>? where T : Any {
            // Load MapperUtils to provide correct mapper factory
            Class.forName("io.github.newagewriter.processor.mapper.GeneratedMapperFactory")
            return Factory.of(value)
        }

        /**
         * Method convert given map to object of given type using proper mapper
         * @param objClass - type of object
         * @param map - map with values
         * @return object of given type or null if that conversion cannot be made
         */
        @JvmStatic
        fun<T> toObject(objClass: Class<T>, map: Map<String, Any?>): T? where T : Any {
            Class.forName("io.github.newagewriter.processor.mapper.GeneratedMapperFactory")
            val mapper = Factory.forClass(objClass, map)
            return mapper?.getMappedObj()
        }

        /**
         * Method convert json inside given stream to object of given type.
         * @param objClass - type to convert
         * @param stream - stream containing json
         * @param charset - Charset format for given stream
         * @return object of given type or null if that conversion cannot be made
         */
        @JvmStatic
        fun<T> fromJsonToObject(objClass: Class<T>, stream: InputStream, charset: Charset = Charsets.UTF_8): T? where T : Any {
            return toObject(objClass, JsonWrapper.jsonToMap(stream, charset))
        }

        /**
         * Method convert json string to object of given type
         * @param objClass - type to convert
         * @param jsonString - json string
         * @return object of given type or null if that conversion cannot be made
         */
        @JvmStatic
        fun<T> fromJsonToObject(objClass: Class<T>, jsonString: String): T? where T : Any {
            return toObject(objClass, JsonWrapper.jsonToMap(jsonString))
        }

        /**
         * Method convert json inside given stream to list containing object of given type.
         * @param objClass - type to convert
         * @param stream - stream containing json
         * @param charset - Charset format for given stream
         * @return array containing object of given type or empty list if that conversion cannot be made
         */
        @JvmStatic
        fun<T> fromJsonToArray(objClass: Class<T>, stream: InputStream, charset: Charset = Charsets.UTF_8): List<T?> where T : Any {
            return JsonWrapper.jsonToArray(stream, charset).map { map ->
                toObject(objClass, map)
            }.toList()
        }

        /**
         * Method convert given json string to list containing object of given type.
         * @param objClass - type to convert
         * @param jsonString - string containing json
         * @return array containing object of given type or empty list if that conversion cannot be made
         */
        @JvmStatic
        fun<T> fromJsonToArray(objClass: Class<T>, jsonString: String): List<T?> where T : Any {
            return JsonWrapper.jsonToArray(jsonString).map { map ->
                toObject(objClass, map)
            }.toList()
        }

        private fun getColorJavaConverters(): Pair<String, GenericConverter<*, *>>? {
            try {
                val clazz = Class.forName("java.awt.Color")
                val converterClass: Class<GenericConverter<*, *>> = Class.forName("io.github.newagewriter.processor.converter.ColorConverter") as Class<GenericConverter<*, *>>
                return clazz.simpleName to converterClass.getDeclaredConstructor().newInstance()
            } catch (ex: ClassNotFoundException) {
                return null
            }
        }
    }
}
