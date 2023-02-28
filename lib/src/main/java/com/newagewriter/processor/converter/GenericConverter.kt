package com.newagewriter.processor.converter

interface GenericConverter<E, V> {

    abstract fun toSimpleValue(entity: E): V

    abstract fun toEntity(value: V): E
}