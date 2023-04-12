package io.github.newagewriter.processor

import java.io.Writer
import java.util.Date
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.StandardLocation

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
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun logD(tag: String, msg: String) {
        val time = Date()
        writer?.write("$time - $tag : $msg \n")
    }

    fun stop() {
        try {
            writer?.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            writer = null
        }
    }
}
