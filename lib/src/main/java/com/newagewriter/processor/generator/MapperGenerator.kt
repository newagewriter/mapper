package com.newagewriter.processor.generator

import com.newagewriter.processor.ProcessorLogger
import com.newagewriter.processor.converter.MapperConverter
import java.lang.StringBuilder
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.VariableElement
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

        fun addSuperClass(classObj: Class<out Any>): Builder {
            return addSuperClass(classObj.`package`.name, classObj.simpleName)
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
        content.append(ClassGenerator()
            .setClassSignature("${mapperName}Mapper(mappedObject : ${el.simpleName}?, map: Map<String, Any?>? = null)")
            .setPackage("${packageName.qualifiedName}.mapper")
            .addImport("com.newagewriter.processor.mapper.AbstractMapper")
            .addImport("${packageName.qualifiedName}.${el.simpleName}")
            .setSuperClassSignature("", "AbstractMapper<${mapperName}>(mappedObject, map)")
            .overrideMethod("toMap", emptyList(), "Map<String, Any?>", generateToMapMethodContent(el))
            .overrideMethod("createMappedObj", emptyList(), el.simpleName.toString(), generateFromMapMethodContent(el))
            .generate()
        )

        try {
            val file = processingEnv.filer.createResource(
                sourceOutput,
                "$moduleAndPkg",
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

    private fun generateToMapMethodContent(element: Element): String {

        val builder = StringBuilder()
        val indent = "    "
        return builder
            .appendLine("return obj?.let { o ->")
            .append("${indent}val result = mapOf<String, Any?>(\n")
            .append("$indent    ${addMappedField(element)}")
            .append("${indent})\n")
//            .append(prepareMappedField(element))
            .append("${indent}result\n")
            .append("} ?: throw NullPointerException(\"Cannot convert object toMap for null value\")\n")
            .toString()
    }

    private fun generateFromMapMethodContent(element: Element): String {

        val builder = StringBuilder()
        val indent = "    "
        StringBuilder::class.java
        val fields = getFieldsName(element)

        builder.appendLine("return  objMap?.let { map -> ")
        builder.appendLine("val convertedMap = mutableMapOf<String, Any?>()")
        fields.forEach { field ->
            builder.appendLine("if (map.contains(\"${field.simpleName}\")) {")
            builder.appendLine("${indent}convertedMap[\"${field.simpleName}\"] = ${MapperConverter.getKotlinClassName(field)}")
            builder.appendLine("}")
        }
        builder.appendLine("createFromMap(convertedMap, ${element.simpleName}::class)")

        builder.appendLine("} ?: throw NullPointerException(\"Map is null, cannot create object\")")
        return builder.toString()
    }

    private fun getFieldsName(element: Element): List<Element> {
        return element.enclosedElements.filter { it.javaClass.simpleName == "VarSymbol" }
    }

    private fun addMappedField(element: Element): String {
        val indent = "    "
        val builder = StringBuilder()
        var first = true
        element.enclosedElements.forEach { el ->
            if (el.javaClass.simpleName == "VarSymbol") {
                if (!first) {
                    builder.append(",\n")
                }
                first = false
                builder.append("$indent\"${el.simpleName}\" to getValue(o.${el.simpleName})")
            }
        }
        builder.append("\n")
        return builder.toString()
    }
}