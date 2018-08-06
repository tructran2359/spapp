package com.spgroup.spapp.repository.mapper

interface IMapper<in E, out M> {

    fun transform(entity: E): M

    fun transform(listOfE: List<E>): List<M> {
        return listOfE.map { transform(it) }
    }
}