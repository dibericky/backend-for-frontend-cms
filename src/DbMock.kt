package com.cms.bff.dibericky

import com.cms.bff.dibericky.models.CustomView
import com.cms.bff.dibericky.models.GalleryCustomView
import com.cms.bff.dibericky.models.getSpecificCustomViewByType
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

interface DbMock<T> {
    fun get () : List<T>
    fun get(id: String) : T?
    fun post (item: T) : String
}
object CustomViewsMock : DbMock<CustomView> {
    override fun get () : List<CustomView> = db.customViews.values.toList()
    override fun post (item: CustomView) : String {
        val id = UUID.randomUUID().toString()
        item.id = id
        db.customViews.putIfAbsent(id, item)
        return id
    }

    override fun get(id: String): CustomView? {
        return db.customViews[id]
    }
}
private object db {
    private val mapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    var customViews : MutableMap<String, CustomView> = mutableMapOf()
    init {
        initCustomViews()
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