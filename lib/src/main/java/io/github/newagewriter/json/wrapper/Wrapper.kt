package com.newagewriter.json.wrapper

import kotlin.reflect.KClass

annotation class Wrapper(val classToWrapped: KClass<*>)
