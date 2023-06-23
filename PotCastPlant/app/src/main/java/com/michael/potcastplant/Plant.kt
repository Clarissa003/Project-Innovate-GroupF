package com.michael.potcastplant

data class Plant(
    var plant_id: Long,
    var plant_name: String,
    var description: String,
    var humidity_max: Long,
    var humidity_min: Long,
    var sunlight_max: Long,
    var sunlight_min: Long,
    var moisture_max: Long,
    var moisture_min: Long,
    var image_url: String
)