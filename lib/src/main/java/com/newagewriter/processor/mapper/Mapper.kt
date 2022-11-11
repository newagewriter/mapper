package com.newagewriter.processor.mapper

/**
 * Annotation class used to prepare mapper for class marked with this annotation
 *
 * For example check UserMapperExample.tk file
 * */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Mapper