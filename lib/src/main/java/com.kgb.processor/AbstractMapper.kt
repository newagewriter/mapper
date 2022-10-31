package com.kgb.processor

abstract class AbstractMapper<T>(protected val mappedObj: T) {
    abstract fun toMap(): Map<String, Any?>

//    abstract fun fromMap(): T
}