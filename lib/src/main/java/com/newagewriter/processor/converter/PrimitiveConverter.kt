package com.newagewriter.processor.converter

import java.math.BigDecimal
import java.math.BigInteger

object PrimitiveConverter {
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
            is BigInteger -> element.toShort()
            is Short -> element
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Short")
        }
    }

    fun toByte(element: Any): Byte {
        return when (element) {
            is Int -> element.toByte()
            is Short -> element.toByte()
            is BigInteger -> element.toByte()
            is Byte -> element
            else -> throw java.lang.ClassCastException("Cannot cast ${element.javaClass.simpleName} to Short")
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
