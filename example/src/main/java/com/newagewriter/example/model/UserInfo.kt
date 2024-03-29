package com.newagewriter.example.model

import io.github.newagewriter.mapper.Mapper
import java.util.Date

@Mapper
data class UserInfo(
    val avatarUrl: String,
    val weight: Float,
    val lastUpdate: Date
) {
    constructor(avatarUrl: String, weight: Float, currentTime: Long): this(avatarUrl, weight, Date(currentTime))
}
