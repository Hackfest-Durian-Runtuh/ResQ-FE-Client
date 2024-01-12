package com.example.resq.model.domain.map

data class MapEmergencyProviderDomain(
    val em_pvd_id:String,
    val longitude:Double,
    val latitude:Double,
    val name:String,
    val em_type:String
)