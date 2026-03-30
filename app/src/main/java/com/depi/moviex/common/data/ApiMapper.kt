package com.depi.moviex.common.data

interface ApiMapper <Domain,Entity>{
    fun mapToDomain(entity: Entity): Domain
}
