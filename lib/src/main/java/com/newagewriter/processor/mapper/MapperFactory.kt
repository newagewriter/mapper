package com.newagewriter.processor.mapper

interface MapperFactory {
    fun<T> of(obj: T): AbstractMapper<T>?
}