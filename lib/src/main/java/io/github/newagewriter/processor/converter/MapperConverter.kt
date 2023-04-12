package io.github.newagewriter.processor.converter

import java.io.InvalidClassException
import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind

object MapperConverter {
    fun getKotlinClassName(field: Element): String {
        return when (field.asType().toString()) {
            "int",
            "java.lang.Integer" -> "map[\"${field.simpleName}\"] as Int"
            "byte",
            "java.lang.Byte" -> "map[\"${field.simpleName}\"] as Byte"
            "float",
            "java.lang.Float" -> "map[\"${field.simpleName}\"] as Float"
            "boolean",
            "java.lang.Boolean" -> "map[\"${field.simpleName}\"] as Boolean"
            "short",
            "java.lang.Short" -> "(map[\"${field.simpleName}\"] as Int).toShort()"
            "long",
            "java.lang.Long" -> "map[\"${field.simpleName}\"] as Long"
            "char",
            "java.lang.Character " -> "map[\"${field.simpleName}\"] as Char"
            "java.lang.String" -> "map[\"${field.simpleName}\"] as String"
            "java.awt.Color" -> "java.awt.Color(map[\"${field.simpleName}\"] as Int)"
            "java.util.Date" -> "java.util.Date(map[\"${field.simpleName}\"] as Long)"
            else -> convertComplexObject(field)
        }
    }

    fun getKotlinTypeForElement(field: Element): String {
        return when (field.asType().toString()) {
            "int",
            "java.lang.Integer" -> "Int"
            "byte",
            "java.lang.Byte" -> "Byte"
            "float",
            "java.lang.Float" -> "Float"
            "double",
            "java.lang.Double" -> "Double"
            "boolean",
            "java.lang.Boolean" -> "Boolean"
            "short",
            "java.lang.Short" -> "Short"
            "long",
            "java.lang.Long" -> "Long"
            "char",
            "java.lang.Character " -> "Char"
            "java.lang.String" -> "String"
            else -> "${field.asType()}"
        }
    }

    private fun convertComplexObject(field: Element): String {
        if (field.asType().kind == TypeKind.DECLARED) {
            return "toType(${field.asType()}::class.java, map[\"${field.simpleName}\"])"
        }
        throw InvalidClassException("Incorrect kind")
    }

    private fun arrayToString(someArray: Array<out Any>): String {
        val result = StringBuilder()
        result.append("[")
        someArray.forEach { el ->
            result.append("$el ")
        }
        result.append("]")
        return result.toString()
    }
}
