package com.kgb.processor

import com.google.auto.service.AutoService
import com.kgb.processor.generator.MapperGenerator
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
            val packageName = processingEnv.elementUtils.getPackageOf(el)
            val mapperBuilder = MapperGenerator.Builder(el, processingEnv)
            mapperBuilder
                .addSuperClass("com.kgb.processor", "AbstractMapper")
                .addExtraImport("${packageName.qualifiedName}.${el.simpleName}")
                .setFileLocation(StandardLocation.SOURCE_OUTPUT)
                .setModuleAndPkg(packageName.qualifiedName.toString())
                .build()
                .generateFile()
        }
        return true
    }
}