package com.kgb.processor

import com.google.auto.service.AutoService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation

@AutoService(Process::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.kgb.processor.Mapper")
class MapperProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "process mappers")
        roundEnv?.getElementsAnnotatedWith(Mapper::class.java)?.forEach { el ->
            val mapperName = el.simpleName
            val packageName = processingEnv.elementUtils.getPackageOf(el)
            val finalDescriptor = StringBuilder()
                .append("package ${packageName.qualifiedName}.mapper \n")
                .append("import com.kgb.processor.AbstractMapper \n")
                .append("import ${packageName.qualifiedName}.${el.simpleName} \n\n")
                .append("abstract class ${mapperName}Mapper(mappedObject : ${el.simpleName}) ")
                .append(": AbstractMapper<${mapperName}>(mappedObject) {\n")
//                .append(prepareConvertToObjectMethod(el))
                .append(prepareToMapMethod(el))
                .append("\n}\n")

            try {
                val file = processingEnv.filer.createResource(
                    StandardLocation.SOURCE_OUTPUT,
                    "resources",
                    "${mapperName}Mapper.kt"
                );
                val writer = file.openWriter()
                writer.write(finalDescriptor.toString());
                writer.close();
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return true
    }

//    private fun prepareConvertToObjectMethod(element: Element): String {
//        val builder = StringBuilder()
//        return builder
//            .append("override fun toObject(map: Map<String, Any?>): ${element.simpleName} {\n")
//            .append("val result = ")
//            .append("return result;")
//            .append("\n}")
//            .toString()
//    }

//    private fun prepareFromMapMapperObjectMethod(element: Element): String {
//
//        val builder = StringBuilder()
//        return builder
//            .append("\n")
//            .append("\noverride fun fromMap(map: Map<String, Any?>): ${element.simpleName} {\n")
//            .append("   val result = createMappedObject()\n")
//            .append(prepareMappedField(element))
//            .append("   return result\n}")
//            .append("\n")
//            .toString()
//    }

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

//    private fun prepareMappedField(element: Element): String {
//        val indent = "  "
//        val builder = StringBuilder()
//        element.enclosedElements.forEach { el ->
//            if (el.javaClass.simpleName == "VarSymbol")
//                builder
//                    .append("\n")
//                    .append("${indent}result.${el.simpleName} = map[\"${el.simpleName}\"]")
//                    .append("\n")
//        }
//        return builder.toString()
//    }
}