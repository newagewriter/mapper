package com.newagewriter.processor

import com.google.auto.service.AutoService
import com.newagewriter.processor.generator.ClassGenerator
import com.newagewriter.processor.generator.MapperGenerator
import com.newagewriter.processor.mapper.AbstractMapper
import com.newagewriter.processor.mapper.Mapper
import com.newagewriter.processor.mapper.MapperFactory
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
@SupportedAnnotationTypes("com.newagewriter.processor.mapper.Mapper")
class MapperProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        ProcessorLogger.startLogger(processingEnv)
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "process mappers")
        val mapperList = mutableMapOf<String, String>()
        roundEnv?.getElementsAnnotatedWith(Mapper::class.java)?.forEach { el ->
            val packageName = processingEnv.elementUtils.getPackageOf(el)
            val mapperBuilder = MapperGenerator.Builder(el, processingEnv)
            mapperList[el.simpleName.toString()] = packageName.qualifiedName.toString()
            mapperBuilder
                .addSuperClass(AbstractMapper::class.java)
                .addExtraImport("${packageName.qualifiedName}.${el.simpleName}")
                .setFileLocation(StandardLocation.SOURCE_OUTPUT)
                .setModuleAndPkg(packageName.qualifiedName.toString())
                .build()
                .generateFile()
        }
        val mapperUtilsClass = ClassGenerator()
            .setClassType(ClassGenerator.ClassType.OBJECT)
            .setPackage("com.newagewriter.processor.mapper")
            .addInterface(MapperFactory::class.java)
            .setClassSignature("MapperUtils")
            .setInitBlock("AbstractMapper.Factory = this")
            .overrideMethod("<T> of", listOf("obj: T"), "AbstractMapper<T>?", getOfMethodContent(mapperList))
            .overrideMethod("<T> forClass", listOf("obj: Class<T>, map: Map<String, Any?>"), "AbstractMapper<T>?", getForClassMethodContent(mapperList))

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
        ProcessorLogger.stop()
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

    private fun getForClassMethodContent(mapperList: Map<String, String>): String {
        val builder = StringBuffer()
        builder.appendLine("when(obj.simpleName) {")
        mapperList.forEach {
            builder.appendLine("${it.key}::class.java.simpleName -> return ${it.key}Mapper(null, map) as AbstractMapper<T>")
        }
        builder.appendLine("else -> return null")
        builder.appendLine("}")
        return builder.toString()
    }
}