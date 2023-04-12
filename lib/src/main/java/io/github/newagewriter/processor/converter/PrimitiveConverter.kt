package io.github.newagewriter.processor.converter

import java.math.BigDecimal
import java.math.BigInteger

object PrimitiveConverter {
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
    fun toFloat(element: Any): Float {
        return when (element) {
            is Double -> element.toFloat()
            is Float -> element
            is BigDecimal -> element.toFloat()
            is Int -> element.toFloat()
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Float")
        }
    }

    fun toShort(element: Any): Short {
        return when (element) {
            is Int -> element.toShort()
            is Long -> element.toShort()
            is BigInteger -> element.toShort()
            is Short -> element
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Short")
        }
    }

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

    fun toInt(element: Any): Int {
        return when (element) {
            is Long -> element.toInt()
            is BigInteger -> element.toInt()
            is Int -> element
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Short")
        }
    }
}
