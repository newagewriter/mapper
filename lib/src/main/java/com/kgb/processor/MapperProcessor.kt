package com.kgb.processor

import com.google.auto.service.AutoService
import com.kgb.processor.generator.ClassGenerator
import com.kgb.processor.generator.MapperGenerator
import com.kgb.processor.mapper.Mapper
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation

@AutoService(Process::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.kgb.processor.mapper.Mapper")
class MapperProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "process mappers")
        val mapperList = mutableMapOf<String, String>()
        roundEnv?.getElementsAnnotatedWith(Mapper::class.java)?.forEach { el ->
            val packageName = processingEnv.elementUtils.getPackageOf(el)
            val mapperBuilder = MapperGenerator.Builder(el, processingEnv)
            mapperList[el.simpleName.toString()] = packageName.qualifiedName.toString()
            mapperBuilder
                .addSuperClass("com.kgb.processor", "AbstractMapper")
                .addExtraImport("${packageName.qualifiedName}.${el.simpleName}")
                .setFileLocation(StandardLocation.SOURCE_OUTPUT)
                .setModuleAndPkg(packageName.qualifiedName.toString())
                .build()
                .generateFile()
        }
        val mapperUtilsClass = ClassGenerator()
            .setClassType(ClassGenerator.ClassType.OBJECT)
            .setPackage("com.kgb.processor.mapper")
            .addInterface("com.kgb.processor.mapper","MapperFactory")
            .setClassSignature("MapperUtils")
            .setInitBlock("AbstractMapper.Factory = this")
            .overrideMethod("<T> of", listOf("obj: T"), "AbstractMapper<T>?", getOfMethodContent(mapperList))

        mapperList.forEach { t, u ->
            mapperUtilsClass.addImport("${u}.$t")
            mapperUtilsClass.addImport("${u}.mapper.${t}Mapper")
        }
        try {
            val file = processingEnv.filer.createResource(
                StandardLocation.SOURCE_OUTPUT,
                "com.kgb.processor",
                "MapperUtils.kt"
            )
            val writer = file.openWriter()
            writer.write(mapperUtilsClass.generate().toString())
            writer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return true
    }

    private fun getOfMethodContent(mapperList: Map<String, String>): String {
        val builder = StringBuffer()
        builder.appendLine("when(obj) {")
        mapperList.forEach {
            builder.appendLine("is ${it.key} -> return ${it.key}Mapper(obj) as AbstractMapper<T>")
        }
        builder.appendLine("else -> return null")
        builder.appendLine("}")
        return builder.toString()
    }
}