package com.newagewriter.example.model.broken

import io.github.newagewriter.mapper.Mapper

@Mapper
data class BrokenUser(
    val id: String,
    val info: BrokenUserInfo
)
