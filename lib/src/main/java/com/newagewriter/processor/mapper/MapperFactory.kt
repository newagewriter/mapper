package com.newagewriter.processor.mapper

interface MapperFactory {
    fun<T> of(obj: T): AbstractMapper<T>?

    fun<T> forClass(obj: Class<T>, map: Map<String, Any?>): AbstractMapper<T>?
}