package io.github.newagewriter.processor.converter

import io.github.newagewriter.annotation.Field
import io.github.newagewriter.processor.ProcessorLogger
import io.github.newagewriter.processor.mapper.AbstractMapper
import java.io.InvalidClassException
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.DeclaredType
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

    private fun getKotlinTypeForElement(field: Element): String {
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
            else -> field.asType().toString()
        }
    }

    private fun getTypeForOthers(env: ProcessingEnvironment, field: Element): String {
        val listType = env.elementUtils.getTypeElement("java.util.List").asType()
        if (env.typeUtils.isAssignable(field.asType(), listType)) {
            return "java.util.List:${(field.asType() as DeclaredType).typeArguments[0]}"
        } else {
            return field.asType().toString()
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

    fun getValueForType(field: Element, fieldAnnotation: Field?): String {
        val fieldName = fieldAnnotation?.name ?: field.simpleName.toString()
        ProcessorLogger.logD("[KB]", "field: ${field.asType()}")
        return if (field.asType().toString().startsWith("java.util.List")) {
            val inListType = (field.asType() as DeclaredType).typeArguments[0]
            "MapperConverter.useList($inListType::class.java, map[\"$fieldName\"])"
        } else if (field.asType().toString().startsWith("java.util.Map")) {
            val keyType = (field.asType() as DeclaredType).typeArguments[0]
            val valueType = (field.asType() as DeclaredType).typeArguments[1]
            "MapperConverter.useMap($keyType::class.java, $valueType::class.java, map[\"$fieldName\"])"
        } else {
            val value = getKotlinTypeForElement(field)
            "toType($value::class.java, map[\"${fieldName}\"])"
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun<U> useList(clazz: Class<U>, obj: Any?): List<U>? where U : Any {
        return when (obj) {
            is List<*> -> {
                val result = mutableListOf<U>()
                obj.forEach {
                    when (it) {
                        is Map<*, *> -> {
                            result.add(AbstractMapper.toObject(clazz, it as Map<String, Any?>) as U)
                        }
                        else -> result.add(it as U)
                    }
                }
                result
            }
            is Array<*> -> {
                val result = mutableListOf<U>()
                obj.forEach {
                    when (it) {
                        is Map<*, *> -> {
                            result.add(AbstractMapper.toObject(clazz, it as Map<String, Any?>) as U)
                        }
                        else -> result.add(it as U)
                    }
                }
                result
            }
            else -> null
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun<K, U> useMap(keyClazz: Class<K>, valueClazz: Class<U>, obj: Any?): Map<K, U?>? where U : Any {
        return when (obj) {
            is Map<*, *> -> {
                val result = mutableMapOf<K, U?>()
                obj.forEach {
                    if (it.value != null) result[it.key as K] = it.value as U else result[it.key as K] = null
                }
                result
            }
            else -> null
        }
    }
}
