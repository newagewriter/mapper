package io.github.newagewriter.annotation

/**
 * Annotation used to change serialization field name
 * Is very useful when json contains parameters that did not meet kotlin field format
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(val name: String)
