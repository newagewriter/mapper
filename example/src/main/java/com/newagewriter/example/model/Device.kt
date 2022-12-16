package com.newagewriter.example.model

import com.newagewriter.processor.mapper.Mapper

@Mapper
data class Device(
    val name: String,
    val type: DeviceType
)