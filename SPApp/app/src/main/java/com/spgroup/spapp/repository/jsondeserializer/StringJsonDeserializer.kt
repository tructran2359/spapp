package com.spgroup.spapp.repository.jsondeserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.spgroup.spapp.util.extension.toHtmlSpanned
import java.lang.reflect.Type

class StringJsonDeserializer : JsonDeserializer<String> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): String {
        json?.let {
            if (it.isJsonPrimitive) {
                val string = json.asString
//                doLogD("TestJson", string)
                return string.toHtmlSpanned().toString()
            }
        }
        return ""
    }

}