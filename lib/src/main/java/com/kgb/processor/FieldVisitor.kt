package com.kgb.processor

import javax.lang.model.element.*
import javax.lang.model.util.AbstractElementVisitor8
import javax.lang.model.util.ElementScanner8

class FieldVisitor(private val builder: java.lang.StringBuilder) : AbstractElementVisitor8<Any, Any>() {
    override fun visitVariable(e: VariableElement?, p: Any?): Any {
        builder.append("v: ${e?.simpleName}")
        return e?.simpleName.toString()
    }

    override fun visitPackage(e: PackageElement?, p: Any?): Any {
        return e?.simpleName.toString()
    }

    override fun visitType(e: TypeElement?, p: Any?): Any {
        return e?.simpleName.toString()
    }

    override fun visitExecutable(e: ExecutableElement?, p: Any?): Any {
        return e?.simpleName.toString()
    }

    override fun visitTypeParameter(e: TypeParameterElement?, p: Any?): Any {
        return e?.simpleName.toString()
    }
}