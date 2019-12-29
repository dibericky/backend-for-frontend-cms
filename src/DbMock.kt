package com.cms.bff.dibericky

import com.cms.bff.dibericky.models.CustomViews
import com.cms.bff.dibericky.models.GalleryCustomViews
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object DbMock {
    val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    var customViews : MutableMap<String, CustomViews> = mutableMapOf()
    init {
        initCustomViews()
    }

    fun getSpecificCustomViewByType(type: String, customViewAsMap: Map<String, Any>) : CustomViews{
        val customViewAsString = mapper.writeValueAsString(customViewAsMap)
        return when(type) {
            "gallery" -> {
                mapper.readValue<GalleryCustomViews>(customViewAsString)
            }
            else -> mapper.readValue<CustomViews>(customViewAsString)
        }
    }
    private fun initCustomViews () {
        val jsonCustomViews = "/mocks_data/custom-views.json".asResource()
        val customViewsMap : Map<String, Map<String, Any>> = mapper.readValue(jsonCustomViews)
        customViewsMap.entries.forEach {
            val id = it.key
            val customViewAsMap = it.value
            customViewAsMap["type"]?.let {type ->
                customViews.putIfAbsent(id, getSpecificCustomViewByType(type.toString(), customViewAsMap))
            }
        }
    }
}