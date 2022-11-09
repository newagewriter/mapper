package com.newagewriter.example.model

import com.newagewriter.processor.BaseUser
import com.newagewriter.processor.mapper.Mapper

@Mapper
class User(
    val name: String,
    val id: Int,
    val userInfo: UserInfo,
    val isValidate: Boolean = true,
    val state: UserStatus = UserStatus.ACTIVE

) : BaseUser()