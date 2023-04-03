package com.newagewriter.example.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class Device(
    val name: String,
    val type: DeviceType
)
