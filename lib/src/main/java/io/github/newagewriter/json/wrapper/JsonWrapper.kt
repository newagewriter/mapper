package io.github.newagewriter.json.wrapper

import io.github.newagewriter.json.library.JsonLibrary
import io.github.newagewriter.json.library.JsonLibraryImpl
import java.io.InputStream
import java.nio.charset.Charset

object JsonWrapper {
    private val library: JsonLibrary

    init {
        library = JsonLibraryImpl()
    }

    fun jsonToMap(jsonString: String): Map<String, Any?> {
        return library.jsonToMap(jsonString)
    }

    fun jsonToMap(stream: InputStream, charset: Charset = Charsets.UTF_8): Map<String, Any?> {
        return library.jsonToMap(stream, charset)
    }

    fun jsonToArray(jsonString: String): List<Map<String, Any?>> {
        return library.jsonToArray(jsonString)
    }

    fun jsonToArray(stream: InputStream, charset: Charset = Charsets.UTF_8): List<Map<String, Any?>> {
        return library.jsonToArray(stream, charset)
    }
}
