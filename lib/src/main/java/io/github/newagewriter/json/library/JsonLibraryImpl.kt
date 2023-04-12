package io.github.newagewriter.json.library

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class JsonLibraryImpl : JsonLibrary() {
    override fun jsonToMapInternal(jsonString: String): Map<String, Any?> {
        return (JSONParser().parse(jsonString) as? JSONObject)
            ?.toMap()
            ?.mapKeys { it.key as String } ?: emptyMap()
    }

    override fun jsonToArrayInternal(jsonString: String): List<Map<String, Any?>> {
        return (JSONParser().parse(jsonString) as? JSONArray)
            ?.toArray()
            ?.map {
                (it as JSONObject).toMap().mapKeys { it.key as String }
            } ?: emptyList()
    }
}
