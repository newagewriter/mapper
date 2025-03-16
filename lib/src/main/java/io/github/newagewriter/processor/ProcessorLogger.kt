package io.github.newagewriter.processor

import java.io.IOException
import java.io.PrintStream
import java.io.PrintWriter
import java.io.Writer
import java.util.Date
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.StandardLocation
import kotlin.jvm.Throws

object ProcessorLogger {
    private var writer: Writer? = null

    fun startLogger(processingEnv: ProcessingEnvironment) {
        try {
            val file = processingEnv.filer.createResource(
                StandardLocation.SOURCE_OUTPUT,
                "logs",
                "processor_logs.log"
            )
            writer = file.openWriter()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    fun logD(tag: String, msg: String) {
        val time = Date()
        writer?.write("$time - $tag : $msg \n")
    }

    fun logE(tag: String, msg: String, throws: Throwable) {
        val time = Date()
        writer?.let { wr ->
            wr.write("$time - $tag : $msg, stacktrace: ")
            throws.printStackTrace(PrintWriter(wr))
            wr.write("\n")
        }

    }

    fun stop() {
        try {
            writer?.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            writer = null
        }
    }
}
