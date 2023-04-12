package io.github.newagewriter.json.library

import java.io.InputStream
import java.nio.charset.Charset

abstract class JsonLibrary {
    protected abstract fun jsonToMapInternal(jsonString: String): Map<String, Any?>

    protected abstract fun jsonToArrayInternal(jsonString: String): List<Map<String, Any?>>

    fun jsonToMap(jsonString: String): Map<String, Any?> {
        return jsonToMapInternal(jsonString)
    }

    fun jsonToMap(stream: InputStream, charset: Charset = Charsets.UTF_8): Map<String, Any?> {
        return jsonToMapInternal(stream.reader(charset).readText())
    }

    fun jsonToArray(jsonString: String): List<Map<String, Any?>> {
        return jsonToArrayInternal(jsonString)
    }

    fun jsonToArray(stream: InputStream, charset: Charset = Charsets.UTF_8): List<Map<String, Any?>> {
        return jsonToArrayInternal(stream.reader(charset).readText())
    }
}
