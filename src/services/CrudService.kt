package com.cms.bff.dibericky.services

interface ICrudService<T> {
    fun getList() : List<T>
}
class CrudService<T>(private val dbMock: MutableMap<String, T>) : ICrudService<T>{
    override fun getList() : List<T> {
        return dbMock.values.toList()
    }
}