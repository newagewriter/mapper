package io.github.newagewriter.processor.converter

import java.math.BigDecimal
import java.math.BigInteger

/**
 * PrimitiveConverter instance is a object which helps to make conversion between primitive type
 */
object PrimitiveConverter {

    /**
     * Cast primitive type given type.
     * @param element - value to cast
     * @param type - type to be cast
     * @return object of given type or exception if cast cannot be made
     */
    @Suppress("UNCHECKED_CAST")
    fun<U> castPrimitiveTo(element: Any, type: Class<U>): U {
        return when (type.simpleName) {
            Int::class.java.simpleName -> toInt(element)
            Byte::class.java.simpleName -> toByte(element)
            Short::class.java.simpleName -> toShort(element)
            Float::class.java.simpleName -> toFloat(element)
            Char::class.java.simpleName -> toChar(element)
            else -> element
        } as U
    }

    /**
     * Method check if given class is primitive type or string
     * @return true for all primitive type and string, false otherwise
     */
    @JvmStatic
    fun isPrimitive(type: Class<*>): Boolean {
        return when (type.simpleName) {
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

    /**
     * Method convert primitive type to Char
     * @param element - element to convert
     * @return Char value or exception if cast cannot be made
     */
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    fun toChar(element: Any): Char {
        return when (element) {
            is Character -> element.charValue()
            is Byte -> element.toInt().toChar()
            is Short -> element.toInt().toChar()
            is Int -> element.toChar()
            is String -> element.first()
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Char")
        }
    }

    /**
     * Method convert primitive type to Float
     * @param element - element to convert
     * @return Float value or exception if cast cannot be made
     */
    fun toFloat(element: Any): Float {
        return when (element) {
            is Double -> element.toFloat()
            is Float -> element
            is BigDecimal -> element.toFloat()
            is Int -> element.toFloat()
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Float")
        }
    }

    /**
     * Method convert primitive type to Short
     * @param element - element to convert
     * @return Short value or exception if cast cannot be made
     */
    fun toShort(element: Any): Short {
        return when (element) {
            is Int -> element.toShort()
            is Long -> element.toShort()
            is BigInteger -> element.toShort()
            is Short -> element
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Short")
        }
    }

    /**
     * Method convert primitive type to Byte
     * @param element - element to convert
     * @return Byte value or exception if cast cannot be made
     */
    fun toByte(element: Any): Byte {
        return when (element) {
            is Int -> element.toByte()
            is Long -> element.toByte()
            is Short -> element.toByte()
            is BigInteger -> element.toByte()
            is Byte -> element
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Byte")
        }
    }

    /**
     * Method convert primitive type to Int
     * @param element - element to convert
     * @return Int value or exception if cast cannot be made
     */
    fun toInt(element: Any): Int {
        return when (element) {
            is Long -> element.toInt()
            is BigInteger -> element.toInt()
            is Int -> element
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Short")
        }
    }
}
