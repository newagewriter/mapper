package com.newagewriter.processor.converter

import java.util.*
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Converter(val type: KClass<*>)
