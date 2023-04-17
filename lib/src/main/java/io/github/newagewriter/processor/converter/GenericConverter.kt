package io.github.newagewriter.processor.converter

import io.github.newagewriter.processor.exception.RequiredAnnotationException

/**
 * Basic class for converter. Contains two methods:
 * # [toSimpleValue] - method convert object to primitive value or string
 * # [toEntity] - method convert primitive/string value to object
 * Every class which extend [GenericConverter] must be annotated with [Converter] annotation
 * otherwise throw exception [RequiredAnnotationException]
 * For example check [DateConverter] class
 */
abstract class GenericConverter<E, V> {

    init {
        if (javaClass.getAnnotation(Converter::class.java) == null) {
            throw RequiredAnnotationException("Converter annotation is required to extend this class")
        }
    }

    /**
     * Convert object to primitive/string value
     * @param entity - object to convert
     * @return primitive/string value
     */
    abstract fun toSimpleValue(entity: E): V

    /**
     * Convert primitive/string value to object
     * @param value - primitive/string value to convert
     * @return object of declare class
     */
    abstract fun toEntity(value: V): E
}
