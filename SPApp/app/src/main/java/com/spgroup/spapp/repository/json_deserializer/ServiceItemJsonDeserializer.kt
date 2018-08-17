package com.spgroup.spapp.repository.json_deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.spgroup.spapp.domain.model.AbsServiceItem
import com.spgroup.spapp.domain.model.CheckboxService
import com.spgroup.spapp.domain.model.ComplexCustomisationService
import com.spgroup.spapp.domain.model.MultiplierService
import java.lang.reflect.Type

class ServiceItemJsonDeserializer : JsonDeserializer<AbsServiceItem> {

    companion object {
        const val FIELD_SERVICE_TYPE = "serviceType"
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, parserContext: JsonDeserializationContext?): AbsServiceItem? {
        json?.let {
            return if (json is JsonObject && json.has(FIELD_SERVICE_TYPE)) {
                when (json.getAsJsonPrimitive(FIELD_SERVICE_TYPE).asString) {
                    "service_checkbox" -> parserContext?.deserialize(json, CheckboxService::class.java)
                    "service_multiplier" -> parserContext?.deserialize(json, MultiplierService::class.java)
                    "service_complex_customisation" -> parserContext?.deserialize(json, ComplexCustomisationService::class.java)
                    else -> null
                }
            } else {
                null
            }
        } ?: return null
    }

}