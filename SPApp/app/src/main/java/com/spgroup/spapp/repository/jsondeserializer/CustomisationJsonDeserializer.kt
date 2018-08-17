package com.spgroup.spapp.repository.jsondeserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.spgroup.spapp.domain.model.*
import java.lang.reflect.Type

class CustomisationJsonDeserializer : JsonDeserializer<AbsCustomisation?> {

    companion object {
        const val FIELD_CUSTOMISATION_TYPE = "type"
        const val TYPE_BOOLEAN = "service_boolean_customisation"
        const val TYPE_NUMBER = "service_number_customisation"
        const val TYPE_MATRIX = "service_customisation_matrix"
        const val TYPE_DROPDOWN = "service_dropdown_customisation"
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, parserContext: JsonDeserializationContext?): AbsCustomisation? {
        json?.let {
            return if (json is JsonObject && json.has(FIELD_CUSTOMISATION_TYPE)) {
                when (json.getAsJsonPrimitive(FIELD_CUSTOMISATION_TYPE).asString) {
                    TYPE_BOOLEAN -> parserContext?.deserialize(json, BooleanCustomisation::class.java)
                    TYPE_NUMBER -> parserContext?.deserialize(json, NumberCustomisation::class.java)
                    TYPE_MATRIX -> parserContext?.deserialize(json, MatrixCustomisation::class.java)
                    TYPE_DROPDOWN -> parserContext?.deserialize(json, DropdownCustomisation::class.java)
                    else -> null
                }
            } else {
                null
            }
        } ?: return null
    }

}