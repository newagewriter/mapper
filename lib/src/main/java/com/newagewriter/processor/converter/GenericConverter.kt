package com.newagewriter.processor.converter

interface GenericConverter<E, V> {
    fun toSimpleValue(entity: E): V
    fun toEntity(value: V): E
}
