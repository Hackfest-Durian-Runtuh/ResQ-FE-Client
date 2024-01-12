package com.example.resq.model.external

data class MapboxGeocodingResponse(
    val features:List<MapboxGeocodingData>
)

data class MapboxGeocodingData(
    val properties:MapboxGeocodingProperties
)

data class MapboxGeocodingProperties(
    val place_formatted:String
)