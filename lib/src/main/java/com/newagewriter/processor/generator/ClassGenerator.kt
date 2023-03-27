package com.newagewriter.processor.generator

import kotlin.text.StringBuilder

class ClassGenerator {
    private var packageName: String = ""
    private var className: String? = null
    private var superClass: String? = null
    private var type: ClassType = ClassType.CLASS
    private val interfaces = mutableListOf<String>()
    private val annotations = mutableListOf<String>()
    private val content: StringBuilder = StringBuilder()
    private val imports = mutableListOf<String>()
    private val methods = mutableListOf<MethodInfo>()
    private var initBlock: String? = null

    fun setPackage(packageName: String): ClassGenerator {
        this.packageName = packageName
        return this
    }

    fun setClassSignature(className: String): ClassGenerator {
        this.className = className
        return this
    }

    fun addInterface(packageName: String, interfaceName: String): ClassGenerator {
        interfaces.add(interfaceName)
        if (this.packageName != packageName && imports.contains(packageName)) {
            imports.add(packageName)
        }
        return this
    }

    fun addAnnotation(annotation: String) {
        annotations.add(annotation)
    }

    fun addInterface(classObj: Class<out Any>): ClassGenerator {
        return addInterface(classObj.`package`.name, classObj.simpleName)
    }

    fun setSuperClassSignature(packageName: String, superClassName: String): ClassGenerator {
        superClass = superClassName
        if (this.packageName != packageName && imports.contains(packageName)) {
            imports.add(packageName)
        }
        return this
    }

    fun addImport(extraImport: String): ClassGenerator {
        imports.add(extraImport)
        return this
    }

    fun overrideMethod(methodSignature: String, args: List<String>, returnType: String, methodContent: String, annotations: List<String> = emptyList()): ClassGenerator {
        methods.add(MethodInfo(methodSignature, args, returnType, methodContent, isOverride = true, annotations = annotations))
        return this
    }

    fun addMethod(methodName: String, args: List<String>, returnType: String, methodContent: String, annotations: List<String> = emptyList()): ClassGenerator {
        methods.add(MethodInfo(methodName, args, returnType, methodContent, isOverride = false, annotations = annotations))
        return this
    }

    fun setClassType(type: ClassType): ClassGenerator {
        this.type = type
        return this
    }

    fun generate(): StringBuilder {
        content.appendLine("package $packageName")
        generateImports()
        generateClassSignature()
        initBlock?.let {
            content.appendLine("init {")
            content.appendLine(initBlock)
            content.appendLine("}")
        }
        methods.forEach {
            content.appendLine(generateMethod(it, 1))
        }
        content.appendLine("}")
        return content
    }

    private fun generateMethod(method: MethodInfo, level: Int): String {
        val builder = java.lang.StringBuilder()
        val indent = getIntend(level)
        val overrideParam = if (method.isOverride) "override " else ""
        return builder
            .append("\n")
            .appendLine(method.annotations.reduce { acc, ann -> "$acc\n$ann" })
            .appendLine("$indent${method.visibility.value} ${overrideParam}fun ${method.methodName}(${method.args.joinToString(", ")}): ${method.returnType} {")
            .appendLine("$indent${method.methodContent}")
            .appendLine("$indent}")
            .toString()
    }

    private fun getIntend(level: Int): String {
        var result = ""
        for (i in 0 until level) {
            result += "    "
        }
        return result
    }

    private fun generateImports() {
        imports.forEach { im ->
            content.appendLine("import $im")
        }
    }

    private fun generateClassSignature() {
        if (annotations.size > 0) {
            annotations.forEach {
                content.appendLine(it)
            }
        }
        className?.let {
            content.appendLine("${type.value} $it ${generateSuperClassAndInterfaces()}{")
        }
    }

    private fun generateSuperClassAndInterfaces(): String {
        val result = StringBuilder()
        if (superClass != null) {
            result.append(": $superClass")
        }
        if (interfaces.size > 0) {
            if (result.isEmpty()) {
                result.append(": ")
            } else {
                result.append(", ")
            }
            result.append(interfaces.joinToString(separator = ", ")).append(" \n")
        }
        return result.toString()
    }

    fun setInitBlock(content: String): ClassGenerator {
        this.initBlock = content
        return this
    }

    enum class ClassType(val value: String) {
        INTERFACE("interface"),
        CLASS("class"),
        OBJECT("object"),
        ENUM("enum class")
    }
}
