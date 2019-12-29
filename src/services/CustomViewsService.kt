package com.cms.bff.dibericky.services

import com.cms.bff.dibericky.CustomViewMissingTypeException
import com.cms.bff.dibericky.models.CustomView
import com.cms.bff.dibericky.models.getSpecificCustomViewByType

class CustomViewsService(val crud: CrudService<CustomView>) {
    fun getList() : List<CustomView> {
        return crud.getList()
    }
    fun addItem(body: Map<String, Any>) : CustomView {
        val type : String? = body["type"]?.toString()

        if (type === null) {
            throw CustomViewMissingTypeException()
        }
        val bodyAsCustomView = getSpecificCustomViewByType(type, body)
        return crud.addItem(bodyAsCustomView)
    }
}