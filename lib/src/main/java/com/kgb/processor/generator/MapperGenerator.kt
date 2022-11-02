package com.kgb.processor.generator

import java.lang.StringBuilder
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.tools.StandardLocation

class MapperGenerator private constructor() {
    private var moduleAndPkg: String = "resources"
    private var superClass: Pair<String, String>? = null
    private val extraImports: MutableList<String> = mutableListOf()
    private val content = StringBuilder()
    private lateinit var el: Element
    private lateinit var processingEnv: ProcessingEnvironment
    private lateinit var sourceOutput: StandardLocation
    class Builder(el: Element, processingEnv: ProcessingEnvironment) {

        private val mapper = MapperGenerator()

        init {
            mapper.el = el
            mapper.processingEnv = processingEnv
        }
        fun build(): MapperGenerator {
            return mapper
        }

        fun addSuperClass(packageName: String, superClassName: String): Builder {
            mapper.superClass = Pair(packageName, superClassName)
            return this
        }

        fun addExtraImport(extraImport: String): Builder {
            mapper.extraImports.add(extraImport)
            return this
        }

        fun setFileLocation(sourceOutput: StandardLocation): Builder {
            mapper.sourceOutput = sourceOutput
            return this
        }

        fun setModuleAndPkg(packageName: String): Builder {
            mapper.moduleAndPkg = packageName
            return this
        }
    }

    fun generateFile(): String {
        val packageName = processingEnv.elementUtils.getPackageOf(el)
        val mapperName = el.simpleName
        content
            .append("package ${packageName.qualifiedName}.mapper \n")
            .append("import com.kgb.processor.AbstractMapper \n")
            .append("import ${packageName.qualifiedName}.${el.simpleName} \n\n")
            .append("class ${mapperName}Mapper(mappedObject : ${el.simpleName}) ")
            .append(": AbstractMapper<${mapperName}>(mappedObject) {\n")
//                .append(prepareConvertToObjectMethod(el))
            .append(prepareToMapMethod(el))
            .append("\n}\n")

        try {
            val file = processingEnv.filer.createResource(
                sourceOutput,
                moduleAndPkg,
                "${mapperName}Mapper.kt"
            );
            val writer = file.openWriter()
            writer.write(content.toString())
            writer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return content.toString()
    }

    private fun prepareToMapMethod(element: Element): String {

        val builder = StringBuilder()
        val indent = "  "
        return builder
            .append("\n")
            .append("\noverride fun toMap(): Map<String, Any?> {\n")
            .append("${indent}val result = mapOf(\n")
            .append("${addMappedField(element)}")
            .append("   )\n")
//            .append(prepareMappedField(element))
            .append("   return result\n}")
            .append("\n")
            .toString()
    }

    private fun addMappedField(element: Element): String {
        val indent = "      "
        val builder = StringBuilder()
        var first = true
        element.enclosedElements.forEach { el ->
            if (el.javaClass.simpleName == "VarSymbol") {
                if (!first) {
                    builder.append(",\n")
                }
                first = false
                builder.append("$indent\"${el.simpleName}\" to mappedObj.${el.simpleName}")
            }
        }
        builder.append("\n")
        return builder.toString()
    }
}