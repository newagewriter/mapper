package com.kgb

import com.kgb.processor.BaseUser
import com.kgb.processor.mapper.Mapper

@Mapper
class User(
    val name: String,
    val id: Int,
    val userInfo: UserInfo,
    val weight: Float,
    val isValidate: Boolean = true,
    val state: UserStatus = UserStatus.ACTIVE

) : BaseUser()