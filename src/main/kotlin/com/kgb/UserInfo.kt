package com.kgb

import com.kgb.processor.mapper.Mapper
import java.util.Date

@Mapper
data class UserInfo(
    val avatarUrl: String,
    val lastUpdate: Date
) {
    constructor(avatarUrl: String, currentTime: Long): this(avatarUrl, Date(currentTime))
}