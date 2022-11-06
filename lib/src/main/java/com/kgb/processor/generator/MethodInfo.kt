package com.kgb.processor.generator

data class MethodInfo(
    val methodName: String,
    val args: List<String>,
    val returnType: String,
    val methodContent: String,
    val visibility: MethodVisibility = MethodVisibility.PUBLIC,
    val isOverride: Boolean = false,
    val isOpen: Boolean = false,

    ) {

    enum class MethodVisibility(val value: String) {
        PUBLIC("public"),
        PRIVATE("private"),
        PROTECTED("protected"),
        //For java class only
        PACKAGE("package");
    }
}