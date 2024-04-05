package io.github.newagewriter.processor.mapper

import io.github.newagewriter.json.wrapper.JsonWrapper
import io.github.newagewriter.processor.converter.DateConverter
import io.github.newagewriter.processor.converter.GenericConverter
import io.github.newagewriter.processor.converter.PrimitiveConverter
import io.github.newagewriter.processor.exception.MissingMapperException
import java.io.InputStream
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
    protected var clazz: Class<T>
) where T : Any {
    private var needRefresh = false
    protected var obj: T? = null
    protected var objMap: Map<String, Any?>? = null

    @Deprecated("use constructor with class instance param instead. Deprecated since 0.4.0, will be removed in 0.5.0")
    constructor(obj: T?, map: Map<String, Any?>?) : this(obj?.javaClass ?: throw ExceptionInInitializerError("obj is null cannot get class instance")) {
        this.obj = obj
        this.objMap = map
    }

    /**
     * Method convert object [obj] to Map
     * @param obj - instance of [T] class
     * @return map with all fields from object [obj]
     */
    abstract fun toMap(obj: T): Map<String, Any?>

    /**
     * Method convert object [obj] to Map
     * @return map with all fields from object [obj]
     */
    @Deprecated("use toMap with [T] params instead", replaceWith = ReplaceWith("this.toMap(model)"))
    fun toMap(): Map<String, Any?> {
        return toMap(obj ?: throw NullPointerException(""))
    }

    /**
     * Method convert map to object
     * @param map - map with properties
     * @return instance of [T] class
     */
    abstract fun mapToModel(map: Map<String, Any?>): T

    /**
     * Method convert map to object
     * @deprecated Since 0.3.2 method is deprecated
     * @return instance of [T] class
     */
    @Deprecated(
        "Use mapToModel(map: Map<String, Any?>) instead",
        replaceWith = ReplaceWith("mapToModel(map)")
    )
    open fun createMappedObj(): T {
        return mapToModel(objMap ?: throw java.lang.NullPointerException("Map is null"))
    }

    @Deprecated(
        "Use toJson(obj: T) instead",
        replaceWith = ReplaceWith("this.toJson(model)")
    )
    fun toJson(): String {
        return toJson(obj ?: throw NullPointerException("Object is null"))
    }

    /**
     * Method convert object of [T] class to json
     * @return Json string for object [obj]
     */
    fun toJson(obj: T): String {
        val result = toMap(obj)
        val builder = StringBuilder()
        builder.appendLine("{")
        var first = true
        result.forEach { entry ->
            if (!first) builder.appendLine(",")
            val value = if (entry.value is String) "\"${entry.value}\"" else entry.value
            builder.append("    \"${entry.key}\" : ${value}")
            first = false
        }
        builder.appendLine("\n}")
        return builder.toString()
    }

    /**
     * Method get object of [T] class.
     * To do this it calls [createMappedObj] to create object before return
     * @param map - map of properties that will be converted to fields
     * @return object of [T] class
     */
    @Deprecated(
        "Use mapToModel(map: Map<String, Any?> instead. Deprecated since 0.4.0, will be removed in 0.5.0 version",
        ReplaceWith("mapToModel(map)")
    )
    fun getMappedObj(map: Map<String, Any?>): T {
        return createMappedObj()
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
                else -> of(v.javaClass)?.toJson(v) ?: "$v"
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
    private fun<T> getConverter(classInfo: Class<T>): GenericConverter<T, Comparable<Any>>? where T : Any {
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
            if (map.keys.containsAll(params.keys)) {
                try {
                    val additionalValues = mutableMapOf<String, Any?>()
                    return c.callBy(
                        map.mapKeys { p ->
                            if (params[p.key] == null) {
                                additionalValues[p.key] = p.value
                            }
                            params[p.key]
                        }.filterKeys { k ->
                            k != null
                        } as Map<KParameter, Any>
                    ).apply {
                        val applyClass = this.javaClass
                        additionalValues.forEach { entry ->
                            println("entry: ${entry.key}")
                            applyClass.getDeclaredField(entry.key)?.let { f ->
                                f.isAccessible = true
                                f.set(this, entry.value)
                            }
                        }
                    }
                } catch (e: IllegalArgumentException) {
                    println("exception: ${e.message}")
                    e.printStackTrace()
                }
            }
        }
        throw InvocationTargetException(Exception("Cannot find constructor for given map: $map"))
    }

    /**
     * Convert json string to model
     * @param jsonString - json to convert
     * @return model of specific class
     */
    fun jsonToModel(jsonString: String): T {
        return mapToModel(JsonWrapper.jsonToMap(jsonString))
    }

    /**
     * Convert stream to model
     * @param stream - stream that contains json
     * @param charset - coding style by default UTF-8
     * @return model of specific class
     */
    fun jsonToModel(stream: InputStream, charset: Charset = Charsets.UTF_8): T {
        return mapToModel(JsonWrapper.jsonToMap(stream, charset))
    }

    /**
     * Convert stream to model
     * @param stream - stream that contains json
     * @param charset - coding style by default UTF-8
     * @return model of specific class
     */
    fun jsonToListOfModels(jsonString: String): List<T?> {
        return JsonWrapper.jsonToArray(jsonString).map { map ->
            of(clazz)?.mapToModel(map)
        }.toList()
    }

    /**
     * Convert stream to model
     * @param stream - stream that contains json
     * @param charset - coding style by default UTF-8
     * @return model of specific class
     */
    fun jsonToListOfModels(stream: InputStream, charset: Charset = Charsets.UTF_8): List<T?> {
        return JsonWrapper.jsonToArray(stream, charset).map { map ->
            of(clazz)?.mapToModel(map)
        }.toList()
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
            } else {
                element as U
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
         * @param clazz - java class instance for [T] type
         * @throws MissingMapperException
         * @return mapper instance for given class or throw exception
         */
        @JvmStatic
        fun<T> ofOrThrow(clazz: Class<T>): AbstractMapper<T> where T : Any {
            return of(clazz) ?: throw MissingMapperException("Cannot find mapper for given class: ${clazz.simpleName}")
        }

        /**
         * Method get proper mapper instance for given object
         * @deprecated
         * This method is no longer acceptable to compute time between versions.
         * <p> Use [of(T::class.java)] instead.
         * @param value - object used to find proper mapper
         * @return mapper for given object or null if mapper for it doesn't exist
         */
        @JvmStatic
        @Deprecated(
            "Use method with clazz param instead. Deprecated since: 0.4.0",
            replaceWith = ReplaceWith("AbstractMapper.of(T::class.java)")
        )
        fun<T> of(value: T): AbstractMapper<T>? where T : Any {
            // Load MapperUtils to provide correct mapper factory
            Class.forName("io.github.newagewriter.processor.mapper.GeneratedMapperFactory")
            return Factory.of(value)
        }

        /**
         * Method get proper mapper instance for given object
         * @param clazz - java class instance for [T] type
         * @return mapper for given object or null if mapper for it doesn't exist
         */
        @JvmStatic
        fun<T> of(clazz: Class<T>): AbstractMapper<T>? where T : Any {
            // Load MapperUtils to provide correct mapper factory
            Class.forName("io.github.newagewriter.processor.mapper.GeneratedMapperFactory")
            return Factory.forClass(clazz)
        }

        /**
         * Method convert given map to object of given type using proper mapper
         * @param objClass - type of object
         * @param map - map with values
         * @return object of given type or null if that conversion cannot be made
         */
        @Deprecated(
            "Use method from mapper instance mapToModel(map: Map<String, Any?>) instead. Deprecated since: 0.4.0",
            replaceWith = ReplaceWith("AbstractMapper.of(T::class.java).mapToModel(map)")
        )
        @JvmStatic
        fun<T : Any> toObject(objClass: Class<T>, map: Map<String, Any?>): T {
            return ofOrThrow(objClass).mapToModel(map)
        }

        /**
         * Method convert json inside given stream to object of given type.
         * @param objClass - type to convert
         * @param stream - stream containing json
         * @param charset - Charset format for given stream
         * @return object of given type or null if that conversion cannot be made
         */
        @Deprecated(
            "Use method from mapper instance jsonToModel(map: Map<String, Any?>) instead. Deprecated since: 0.4.0",
            replaceWith = ReplaceWith("AbstractMapper.of(T::class.java).jsonToModel(stream, charset)")
        )
        @JvmStatic
        fun<T : Any> fromJsonToObject(objClass: Class<T>, stream: InputStream, charset: Charset = Charsets.UTF_8): T? {
            return of(objClass)?.mapToModel(JsonWrapper.jsonToMap(stream, charset))
        }

        /**
         * Method convert json string to object of given type
         * @param objClass - type to convert
         * @param jsonString - json string
         * @return object of given type or null if that conversion cannot be made
         */
        @JvmStatic
        fun<T : Any> fromJsonToObject(objClass: Class<T>, jsonString: String): T? {
            return of(objClass)?.mapToModel(JsonWrapper.jsonToMap(jsonString))
        }

        /**
         * Method convert json inside given stream to list containing object of given type.
         * @param objClass - type to convert
         * @param stream - stream containing json
         * @param charset - Charset format for given stream
         * @return array containing object of given type or empty list if that conversion cannot be made
         */
        @JvmStatic
        fun<T : Any> fromJsonToArray(objClass: Class<T>, stream: InputStream, charset: Charset = Charsets.UTF_8): List<T?> {
            return JsonWrapper.jsonToArray(stream, charset).map { map ->
                of(objClass)?.mapToModel(map)
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
                of(objClass)?.mapToModel(map)
            }.toList()
        }

        private fun getColorJavaConverters(): Pair<String, GenericConverter<*, *>>? {
            return try {
                val clazz = Class.forName("java.awt.Color")
                val converterClass: Class<GenericConverter<*, *>> = Class.forName("io.github.newagewriter.processor.converter.ColorConverter") as Class<GenericConverter<*, *>>
                clazz.simpleName to converterClass.getDeclaredConstructor().newInstance()
            } catch (ex: ClassNotFoundException) {
                null
            }
        }
    }
}
