package io.github.newagewriter.processor

import com.google.auto.service.AutoService
import io.github.newagewriter.mapper.Mapper
import io.github.newagewriter.processor.converter.Converter
import io.github.newagewriter.processor.generator.MapperGenerator
import io.github.newagewriter.processor.mapper.AbstractMapper
import io.github.newagewriter.template.TemplateLoader
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.script.ScriptEngineManager
import javax.tools.Diagnostic
import javax.tools.StandardLocation

@AutoService(Process::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = ["io.github.newagewriter.mapper.Mapper", "com.newagewriter.processor.converter.Converter"])
class MapperProcessor : AbstractProcessor() {
    override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        try {
            ProcessorLogger.startLogger(processingEnv)
            ProcessorLogger.logD("[KB]", "----- START LOGGIN -----")
            val scriptManager = ScriptEngineManager()
            scriptManager.getEngineByName("kotlin")?.let { engine ->
                val myString = "test"
                val value = engine.eval("\"com.st\" == \"\"")
            }

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
            val mapperUtilsTemplate = TemplateLoader.load("GeneratedMapperFactory")
            mapperUtilsTemplate
                .addVariable("types", mapperList.keys)
                .addVariable("mapperList", mapperList)
            val file = processingEnv.filer.createResource(
                StandardLocation.SOURCE_OUTPUT,
                "com.newagewriter.processor.mapper",
                "GeneratedMapperFactory.kt"
            )
            val writer = file.openOutputStream()
            writer.write(mapperUtilsTemplate.compile().toByteArray(Charsets.UTF_8))
            writer.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ProcessorLogger.logD("[KB]", "exception: $ex")
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
            val converterName = "$elementPackage.${e.simpleName}"
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

        try {
            val file = processingEnv.filer.createResource(
                StandardLocation.SOURCE_OUTPUT,
                "com.newagewriter.processor.converter",
                "ConverterUtils.kt"
            )
            val writer = file.openWriter()
            writer.write(converterTemplate.compile())
            writer.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        ProcessorLogger.stop()
    }
}
