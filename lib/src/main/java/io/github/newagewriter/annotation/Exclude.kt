package io.github.newagewriter.annotation

/**
 * Annotation used to exclude a field from serialization
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Exclude
