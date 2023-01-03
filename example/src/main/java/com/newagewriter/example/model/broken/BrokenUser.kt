package com.newagewriter.example.model.broken

import com.newagewriter.processor.mapper.Mapper

@Mapper
data class BrokenUser(
    val id: String,
    val info: BrokenUserInfo
)