package com.cms.bff.dibericky.models

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

open class CustomView(
    var id: String?,
    var name: String,
    var type: String
)

class GalleryCustomView(
    id: String?,
    name: String,
    var source: String,
    var collection: String,
    var projection: List<String>,
    var metadata: List<String>,
    var categorizedBy: String?
) : CustomView(id=id, name=name, type="gallery")

fun getSpecificCustomViewByType(type: String, customViewAsMap: Map<String, Any>) : CustomView{
    val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    val customViewAsString = mapper.writeValueAsString(customViewAsMap)
    return when(type) {
        "gallery" -> {
            mapper.readValue<GalleryCustomView>(customViewAsString)
        }
        else -> mapper.readValue<CustomView>(customViewAsString)
    }
}