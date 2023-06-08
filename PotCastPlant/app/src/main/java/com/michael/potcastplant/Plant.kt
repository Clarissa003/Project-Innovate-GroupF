package com.michael.potcastplant

data class Plant (
    var plant_id : Int,
    var plant_name: String,
    var description: String,
    var humidity_max: Int,
    var humidity_min: Int,
    var sunlight_max: Int,
    var sunlight_min: Int,
    var moisture_max: Int,
    var moisture_min: Int,
    var image_url: String
)