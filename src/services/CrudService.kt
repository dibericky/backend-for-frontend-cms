package com.cms.bff.dibericky.services

import com.cms.bff.dibericky.DbMock
import java.util.*

interface ICrudService<T> {
    fun getList() : List<T>
    fun addItem(item: T) : T
}
class CrudService<T>(private val dbMock: DbMock<T>) : ICrudService<T>{
    override fun getList() : List<T> {
        return dbMock.get()
    }

    override fun addItem(item: T): T {
        val id = dbMock.post(item)
        return dbMock.get(id)!!
    }
}