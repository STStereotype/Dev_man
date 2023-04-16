package com.example.dev_man.modules.models

data class Module (
    var ip: String,
    var host: String,
    var mask: String = "255.255.255.0",
    var batteryLevel: Int,
    var temperature: Float = 30.0f,
    var nameModule: String
)