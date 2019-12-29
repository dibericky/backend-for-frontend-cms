package com.cms.bff.dibericky.services

import com.cms.bff.dibericky.models.CustomViews

class CustomViewsService(val crud: CrudService<CustomViews>) {
    fun getList() : List<CustomViews> {
        return crud.getList()
    }
}