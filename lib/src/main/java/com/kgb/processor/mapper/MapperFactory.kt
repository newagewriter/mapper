package com.kgb.processor.mapper

interface MapperFactory {
    fun<T> of(obj: T): AbstractMapper<T>?
}