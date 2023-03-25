package com.newagewriter.processor

import com.google.auto.service.AutoService
import com.newagewriter.processor.converter.Converter
import com.newagewriter.processor.generator.ClassGenerator
import com.newagewriter.processor.generator.MapperGenerator
import com.newagewriter.processor.mapper.AbstractMapper
import com.newagewriter.processor.mapper.Mapper
import com.newagewriter.processor.mapper.MapperFactory
import com.newagewriter.template.TemplateLoader
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.tools.Diagnostic
import javax.tools.StandardLocation


@AutoService(Process::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = ["com.newagewriter.processor.mapper.Mapper", "com.newagewriter.processor.converter.Converter"])
class MapperProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        try {
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
            roundEnv?.getElementsAnnotatedWith(Converter::class.java)?.let { generateConverterList(it) }
            val mapperUtilsClass = ClassGenerator()
                .setClassType(ClassGenerator.ClassType.OBJECT)
                .setPackage("com.newagewriter.processor.mapper")
                .addInterface(MapperFactory::class.java)
                .setClassSignature("MapperUtils")
                .setInitBlock("AbstractMapper.Factory = this")
                .overrideMethod("<T> of", listOf("obj: T"), "AbstractMapper<T>? where T : Any", getOfMethodContent(mapperList))
                .overrideMethod("<T> forClass", listOf("obj: Class<T>, map: Map<String, Any?>"), "AbstractMapper<T>? where T : Any", getForClassMethodContent(mapperList))

            mapperList.forEach { t, u ->
                mapperUtilsClass.addImport("${u}.$t")
                mapperUtilsClass.addImport("${u}.mapper.${t}Mapper")
            }
            val file = processingEnv.filer.createResource(
                StandardLocation.SOURCE_OUTPUT,
                "com.newagewriter.processor.mapper",
                "MapperUtils.kt"
            )
            val writer = file.openWriter()
            writer.write(mapperUtilsClass.generate().toString())
            writer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            ProcessorLogger.stop()
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

    private fun generateConverterList(elements: Set<Element>) {
        val map = mutableMapOf<String, String>()
        elements.forEach { e ->
            val converterAnnotation: Converter = e.getAnnotation(Converter::class.java)
            val elementPackage = processingEnv.elementUtils.getPackageOf(e)
            val converterName = "${elementPackage}.${e.simpleName}"
            val reg = Regex("type=(([a-zA-z]+\\.)*)([a-zA-z_0-9]+)").find(converterAnnotation.toString())
            val typeName = reg?.let {
                val g = it.groupValues
                if (g[0].endsWith(".class")) {
                    g[2].replace(".", "")
                } else {
                    g[3]
                }
            } ?: ""
            map[typeName] = converterName
        }
        val converterTemplate = TemplateLoader.load("ConverterUtils")
        converterTemplate.addVariable("map", map)
        val converterModule = ClassGenerator()
            .setClassType(ClassGenerator.ClassType.OBJECT)
            .setPackage("com.newagewriter.processor.converter")
            .setClassSignature("ConverterUtils")
            .addMethod(
                "initConverters",
                emptyList(),
                "Map<String, GenericConverter<*, *>>",
                getInitConverterMethodContent(elements),
                annotations = listOf("@JvmStatic")
            )
//            .addImport("com.newagewriter.processor.converter")

        try {
            val file = processingEnv.filer.createResource(
                StandardLocation.SOURCE_OUTPUT,
                "com.newagewriter.processor.converter",
                "ConverterUtils.kt"
            )
            val writer = file.openWriter()
            writer.write(converterTemplate.compile())
            writer.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        ProcessorLogger.stop()
    }

    private fun getInitConverterMethodContent(elements: Set<Element>): String {
        val result = StringBuilder()
        result.appendLine("val converters = HashMap<String, GenericConverter<*, *>>()")
        elements.forEach { e ->
            val converterAnnotation: Converter = e.getAnnotation(Converter::class.java)
            val elementPackage = processingEnv.elementUtils.getPackageOf(e)
            val converterName = "${elementPackage}.${e.simpleName}"
            val reg = Regex("type=(([a-zA-z]+\\.)*)([a-zA-z_0-9]+)").find(converterAnnotation.toString())
            val typeName = reg?.let {
                val g = it.groupValues
                if (g[0].endsWith(".class")) {
                    g[2].replace(".", "")
                } else {
                    g[3]
                }
            } ?: ""
            result.appendLine("converters.put(\"${typeName}\", ${converterName}())")
        }
        result.appendLine("return converters")
        return result.toString()
    }
}